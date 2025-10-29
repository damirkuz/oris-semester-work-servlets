package ru.kuzdikenov.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.dto.UuidAndLoginAndPath;
import ru.kuzdikenov.entity.Image;
import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.entity.User;
import ru.kuzdikenov.exception.*;
import ru.kuzdikenov.service.ImageService;
import ru.kuzdikenov.service.InitiativeService;
import ru.kuzdikenov.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitiativeUtil {

    private static final Logger log = LoggerFactory.getLogger(InitiativeUtil.class);
    public static final int MIN_TITLE_LENGTH = 5;
    public static final int MAX_TITLE_LENGTH = 120;

    public static boolean isValidTitle(String title) {
        log.atInfo().log("Проверяю заголовок " + title);
        if (title.length() < MIN_TITLE_LENGTH || title.length() > MAX_TITLE_LENGTH) {
            log.atError().log("Заголовок " + title + "некорректной длины");
            return false;
        }
        return true;
    }

    public static Initiative loadInitiativeOr404(int initiativeId, String sessionLogin, InitiativeService initiativeService, HttpServletResponse resp) throws IOException {
        try {
            return initiativeService.getById(initiativeId, sessionLogin);
        } catch (InitiativeNotFoundInDatabaseException e) {
            log.atError().log(e.getMessage());
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    public static boolean isSelfInitiative(Initiative initiative, String sessionLogin, UserService userService) {
        try {
            User creator = userService.getById(initiative.getCreatorUserId());
            return creator.getLogin().equals(sessionLogin);
        } catch (UserNotFoundInDatabaseException e) {
            return false;
        }
    }

    public static List<Image> getImagesFromForm(ImageService imageService, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
