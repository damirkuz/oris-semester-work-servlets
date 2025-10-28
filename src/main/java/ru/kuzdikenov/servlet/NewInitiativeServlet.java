package ru.kuzdikenov.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.dto.UuidAndLoginAndPath;
import ru.kuzdikenov.entity.Image;
import ru.kuzdikenov.exception.*;
import ru.kuzdikenov.helper.ImageUtil;
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
import java.util.ArrayList;
import java.util.List;

@MultipartConfig
@WebServlet(name = "NewInitiative", urlPatterns = "/new-initiative/*")
public class NewInitiativeServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(NewInitiativeServlet.class);

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
        String userLoginFromSession = (String) req.getSession().getAttribute("login");

        if (userLoginFromSession != null && !userLoginFromSession.isEmpty()) {
            req.getRequestDispatcher("/new_initiative.ftl").forward(req, resp);
        } else {
            log.atInfo().log("Незарегистрированный пользователь пытался опубликовать инициативу");
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userLoginFromSession = (String) req.getSession().getAttribute("login");
        if (userLoginFromSession == null || userLoginFromSession.isEmpty()) {
            log.atInfo().log("Незарегистрированный пользователь пытался опубликовать инициативу");
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        log.atInfo().log("Получаем параметры из формы на публикацию инициативы");
        String initiativeTitle = req.getParameter("title");
        String body = req.getParameter("description");

        List<Image> images = getImagesFromForm(req, resp);



        try {
            int initiativeId = initiativeService.save(userLoginFromSession, initiativeTitle, body, images);
            log.atInfo().log("Инициатива успешно опубликована");
            resp.sendRedirect(req.getContextPath() + "/initiative/" + initiativeId);
        } catch (InvalidInitiativeTitleException e) {
            resp.sendRedirect(req.getContextPath() + "/new-initiative/" + "?error=invalidInitiativeTitle");
        } catch (FailInitiativeSaveException e) {
            resp.sendRedirect(req.getContextPath() + "/new-initiative/" + "?error=failInitiativeSave");
        }
    }

    private List<Image> getImagesFromForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Image> result = new ArrayList<>();

        try {
            List<UuidAndLoginAndPath> images = ImageUtil.handlePhotosAndSaveToCloudinary(req, "photos");
            for (UuidAndLoginAndPath u : images) {
                imageService.save(u.uuid(), u.login(), u.path());
                result.add(imageService.getByUuid(u.uuid()));
            }
            log.atInfo().log("Фотографии успешно обработаны");
        } catch (InvalidImageExtensionException e) {
            resp.sendRedirect(req.getContextPath() + "/new-initiative/" + "?error=invalidImageExtension");
        } catch (InvalidImageSizeException e) {
            resp.sendRedirect(req.getContextPath() + "/new-initiative/" + "?error=invalidImageSize");
        } catch (InvalidImageNameException e) {
            resp.sendRedirect(req.getContextPath() + "/new-initiative/" + "?error=invalidImageName");
        } catch (IOException e) {
            log.atError().log("Не удалось создать директории");
            throw new RuntimeException(e);
        }

        return result;
    }
}
