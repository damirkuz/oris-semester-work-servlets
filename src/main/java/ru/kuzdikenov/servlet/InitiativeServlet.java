package ru.kuzdikenov.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.dto.UserProfile;
import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.entity.User;
import ru.kuzdikenov.exception.InitiativeNotFoundInDatabaseException;
import ru.kuzdikenov.exception.UserNotFoundInDatabaseException;
import ru.kuzdikenov.helper.UrlUtil;
import ru.kuzdikenov.service.ImageService;
import ru.kuzdikenov.service.InitiativeService;
import ru.kuzdikenov.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Initiative", urlPatterns = "/initiative/*")
public class InitiativeServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(InitiativeServlet.class);

    private static UserService userService;
    private static ImageService imageService;
    private static InitiativeService initiativeService;

    @Override
    public void init() {
        this.userService = (UserService) getServletContext().getAttribute("userService");
        this.imageService = (ImageService) getServletContext().getAttribute("imageService");
        this.initiativeService = (InitiativeService) getServletContext().getAttribute("initiativeService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String initiativeId = UrlUtil.getUrlAfterSlash(req, resp, 1);

        if (initiativeId == null) {
            return; // go to 404
        }

        try {
            Initiative initiative = initiativeService.getById(initiativeId);


            log.atInfo().log("Проверяем, что пользователь открыл свою же инициативу");
            String userLoginFromSession = (String) req.getSession().getAttribute("login");

            User creatorUser = userService.getById(initiative.getCreatorUserId());

            boolean isSelfUserInitiative = creatorUser.getLogin().equals(userLoginFromSession);
            log.atInfo().log("Пользователь открыл " + (isSelfUserInitiative ? "свою": "чужую") + " инициативу");

            boolean likedByMe = initiativeService.checkUserLiked(userLoginFromSession, initiative.getInitiativeId());

            req.setAttribute("isSelfUserInitiative", isSelfUserInitiative);
            req.setAttribute("likedByMe", likedByMe);
            req.setAttribute("initiative", initiative);

            req.getRequestDispatcher("/initiative.ftl").forward(req, resp);
        } catch (UserNotFoundInDatabaseException | InitiativeNotFoundInDatabaseException e) {
            log.atError().log(e.getMessage());
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.atInfo().log("Получаем логин пользователя из строки");
        int initiativeId = Integer.parseInt(UrlUtil.getUrlAfterSlash(req, resp, 1).split("/")[0]);

        String userAction = UrlUtil.getUrlAfterSlash(req, resp, 2); // for example: like, edit, comment

        String userLoginFromSession = (String) req.getSession().getAttribute("login");

        switch (userAction) {
            case "like": {
                try {
                    initiativeService.like(userLoginFromSession, initiativeId);
                    resp.sendRedirect("/initiative/" + initiativeId);
                } catch (InitiativeNotFoundInDatabaseException | UserNotFoundInDatabaseException e) {
                    log.atError().log(e.getMessage());
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                break;
            }
            default: return; // 404
        }
    }
}
