package ru.kuzdikenov.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.entity.Image;
import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.entity.InitiativeStatus;
import ru.kuzdikenov.entity.UserRole;
import ru.kuzdikenov.exception.InitiativeNotFoundInDatabaseException;
import ru.kuzdikenov.exception.NoChangesException;
import ru.kuzdikenov.exception.UserNotFoundInDatabaseException;
import ru.kuzdikenov.helper.InitiativeUtil;
import ru.kuzdikenov.helper.UrlUtil;
import ru.kuzdikenov.service.ImageService;
import ru.kuzdikenov.service.InitiativeService;
import ru.kuzdikenov.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@MultipartConfig
@WebServlet(name = "Initiative", urlPatterns = "/initiative/*")
public class InitiativeServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(InitiativeServlet.class);

    private static UserService userService;
    private static ImageService imageService;
    private static InitiativeService initiativeService;

    @Override
    public void init() {
        userService = (UserService) getServletContext().getAttribute("userService");
        imageService = (ImageService) getServletContext().getAttribute("imageService");
        initiativeService = (InitiativeService) getServletContext().getAttribute("initiativeService");
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer initiativeId = UrlUtil.parseIdOr404(req, resp);
        if (initiativeId == null) return;

        String sessionLogin = (String) req.getSession().getAttribute("login");

        boolean isAdmin = false;
        try {
            isAdmin = userService.getByLogin(sessionLogin).getUserRole().equals(UserRole.ADMIN);
        } catch (UserNotFoundInDatabaseException e) {
        }

        req.setAttribute("isAdmin", isAdmin);

        String userAction = UrlUtil.getUrlAfterSlash(req, resp, 2); // for example: edit
        if (userAction != null && userAction.equals("edit")) {
            getEditPage(initiativeId, req, resp);
            return;
        }


        Initiative initiative = InitiativeUtil.loadInitiativeOr404(initiativeId, sessionLogin, initiativeService, resp);
        if (initiative == null) return;

        boolean isSelf = InitiativeUtil.isSelfInitiative(initiative, sessionLogin, userService);
        boolean likedByMe = initiativeService.checkUserLiked(sessionLogin, initiative.getInitiativeId());

        req.setAttribute("isSelfUserInitiative", isSelf);
        req.setAttribute("likedByMe", likedByMe);
        req.setAttribute("initiative", initiative);
        req.getRequestDispatcher("/initiative.ftl").forward(req, resp);
    }

    private void getEditPage(int initiativeId, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String sessionLogin = (String) req.getSession().getAttribute("login");
        Initiative initiative = InitiativeUtil.loadInitiativeOr404(initiativeId, sessionLogin, initiativeService, resp);
        if (initiative == null) return;

        boolean isSelf = InitiativeUtil.isSelfInitiative(initiative, sessionLogin, userService);

        req.setAttribute("initiative", initiative);
        req.setAttribute("initiativeStatuses", InitiativeStatus.values());
        req.getRequestDispatcher("/edit_initiative.ftl").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer initiativeId = UrlUtil.parseIdOr404(req, resp);
        if (initiativeId == null) return;


        String userAction = UrlUtil.getUrlAfterSlash(req, resp, 2);
        String login = requireLoginOrRedirect(req, resp, initiativeId);
        if (login == null) return;

        switch (userAction) {
            case "like": {
                try {
                    initiativeService.like(login, initiativeId);
                    redirectToInitiative(req, resp, initiativeId, null);
                } catch (InitiativeNotFoundInDatabaseException | UserNotFoundInDatabaseException e) {
                    sendNotFound(resp);
                }
                break;
            }

            case "comment": {
                try {
                    String body = req.getParameter("comment");
                    initiativeService.comment(login, initiativeId, body);
                    redirectToInitiative(req, resp, initiativeId, null);
                } catch (InitiativeNotFoundInDatabaseException | UserNotFoundInDatabaseException e) {
                    sendNotFound(resp);
                }
                break;
            }

            case "edit": {
                try {
                    String title = req.getParameter("title");
                    String body = req.getParameter("description");
                    String statusParam = req.getParameter("status");
                    InitiativeStatus status = statusParam == null ? InitiativeStatus.SUGGESTED : InitiativeStatus.valueOf(statusParam);
                    List<Image> images = InitiativeUtil.getImagesFromForm(imageService, req, resp);

                    initiativeService.edit(initiativeId, title, body, images, status);
                    log.atInfo().log("Инициатива " + initiativeId + " успешно отредактирована");
                    resp.sendRedirect(req.getContextPath() + "/initiative/" + initiativeId);
                } catch (NoChangesException e) {
                    log.atError().log("Инициатива " + initiativeId + " не была изменена");
                    resp.sendRedirect(req.getContextPath() + "/initiative/" + initiativeId);
                    throw new RuntimeException(e);
                } catch (InitiativeNotFoundInDatabaseException e) {
                    sendNotFound(resp);
                }
                break;
            }
            default: sendNotFound(resp);
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer initiativeId = UrlUtil.parseIdOr404(req, resp);
        if (initiativeId == null) return;

        String login = requireLoginOrRedirect(req, resp, initiativeId);
        if (login == null) return;

        try {
            initiativeService.delete(initiativeId);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (InitiativeNotFoundInDatabaseException e) {
            redirectToInitiative(req, resp, initiativeId, "?error=deleteInitiativeError");
        }
    }

    private String requireLoginOrRedirect(HttpServletRequest req, HttpServletResponse resp, int initiativeId) throws IOException {
        String login = (String) req.getSession().getAttribute("login");
        if (login == null) {
            resp.sendRedirect(req.getContextPath() + "/initiative/" + initiativeId + "?error=nonAuthorizedUser");
            return null;
        }
        return login;
    }

    private void sendNotFound(HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void redirectToInitiative(HttpServletRequest req, HttpServletResponse resp, int initiativeId, String qs) throws IOException {
        String url = req.getContextPath() + "/initiative/" + initiativeId + (qs != null ? qs : "");
        resp.sendRedirect(url);
    }

}
