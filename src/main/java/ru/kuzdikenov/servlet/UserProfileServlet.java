package ru.kuzdikenov.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.dto.UserProfile;
import ru.kuzdikenov.dto.UuidAndLoginAndPath;
import ru.kuzdikenov.exception.*;
import ru.kuzdikenov.helper.ImageUtil;
import ru.kuzdikenov.helper.UrlUtil;
import ru.kuzdikenov.service.ImageService;
import ru.kuzdikenov.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@MultipartConfig
@WebServlet(name = "User Profile", urlPatterns = "/profile/*")
public class UserProfileServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(UserProfileServlet.class);

    private static UserService userService;
    private static ImageService imageService;

    @Override
    public void init() {
        this.userService = (UserService) getServletContext().getAttribute("userService");
        this.imageService = (ImageService) getServletContext().getAttribute("imageService");
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userLogin = UrlUtil.getUrlAfterSlash(req, resp, 1);

        if (userLogin == null) {
            return; // go to 404
        }

        try {
            UserProfile userProfile = userService.getUserProfile(userLogin);
            req.setAttribute("userProfile", userProfile);


            log.atInfo().log("Проверяем, что пользователь открыл свой же профиль");
            String userLoginFromSession = (String) req.getSession().getAttribute("login");
            boolean isSelfUserProfile = userProfile.getLogin().equals(userLoginFromSession);
            log.atInfo().log("Пользователь открыл " + (isSelfUserProfile ? "свой": "чужой") + " профиль");
            req.setAttribute("isSelfUserProfile", isSelfUserProfile);

            req.getRequestDispatcher("/profile.ftl").forward(req, resp);
        } catch (UserNotFoundInDatabaseException e) {
            log.atError().log(e.getMessage());
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.atInfo().log("Получаем логин пользователя из строки");
        String userLogin = UrlUtil.getUrlAfterSlash(req, resp, 1);

        if (userLogin == null) {
            return; // go to 404
        }

        log.atInfo().log("Получаем логин пользователя из http сессии");
        String userLoginFromSession = (String) req.getSession().getAttribute("login");

        if (!userLogin.equals(userLoginFromSession)) {
            log.atInfo().log("У пользователя нет доступа к редактированию профиля");
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        log.atInfo().log("Получаем параметры из формы на редактирование профиля");
        String name = req.getParameter("name");
        String password = req.getParameter("password");

        UUID imageUuid = null;
        try {
            // save image from form to server
            List<UuidAndLoginAndPath> resList = ImageUtil.handlePhotosAndSaveToCloudinary(req, "avatar");
            if (resList.size() == 1) {
                UuidAndLoginAndPath res = resList.getFirst();
                // save image path to db
                imageService.save(res.uuid(), res.login(), res.path());
                imageUuid = res.uuid();
            }
        } catch (IOException | InvalidImageExtensionException e) {
            // the user didn't send the image
        } catch (InvalidImageSizeException e) {
            throw new RuntimeException(e);
        } catch (InvalidImageNameException e) {
            throw new RuntimeException(e);
        }

        try {
            userService.editProfile(userLoginFromSession, name, password, imageUuid);
            log.atInfo().log("Профиль успешно отредактирован");
            resp.sendRedirect(req.getContextPath() + "/profile/" + userLogin + "?success=true");
        } catch (InvalidPasswordException e) {
            log.atError().log("Некорректный пароль, профиль не изменён");
            resp.sendRedirect(req.getContextPath() + "/profile/" + userLogin + "?error=invalidPassword");
        } catch (NoChangesException e) {
            log.atError().log("Профиль не изменён");
            resp.sendRedirect(req.getContextPath() + "/profile/" + userLogin + "?error=noChanges");
        } catch (UserNotFoundInDatabaseException e) {
            log.atError().log(e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
