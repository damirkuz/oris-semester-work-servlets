package ru.kuzdikenov.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.app.DefaultSettings;
import ru.kuzdikenov.dto.UuidAndLoginAndPath;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Paths;
import java.util.UUID;

@MultipartConfig
public class ImageUtil {
    public static final String FILE_PREFIX = DefaultSettings.FILE_STORAGE_DIR + DefaultSettings.FILE_ACCESS_URL_PATH;
    public static final int DIRECTORIES_COUNT = 100;
    private static final Logger log = LoggerFactory.getLogger(ImageUtil.class);


    private static String getImagePathFromUuid(UUID uuid) {
        StringBuilder sb = new StringBuilder();
        sb.append(File.separator);
        sb.append(String.valueOf(Math.abs( uuid.hashCode() % DIRECTORIES_COUNT)));
        sb.append(File.separator);
        sb.append(uuid.toString());
        return sb.toString();
    }

    public static UuidAndLoginAndPath parseAndSave(HttpServletRequest req, String parameterName) throws ServletException, IOException {
        log.atInfo().log("Получаю картинку из формы");
        Part part = req.getPart(parameterName);

        log.atInfo().log("Получаем логин пользователя из http сессии");
        String userLoginFromSession = (String) req.getSession().getAttribute("login");

        if (part == null || part.getSize() == 0) {
            throw new IOException("part is null");
        }

        log.atInfo().log("Получаю название картинки");
        String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        String filenameExtension = filename.substring(filename.lastIndexOf("."));
        UUID uuid = UUID.randomUUID();

        log.atInfo().log("Генерирую путь для хранения");
        String shortPath = getImagePathFromUuid(uuid) + filenameExtension;
        String path = FILE_PREFIX + shortPath;

        File file = new File(path);

        InputStream content = part.getInputStream();

        file.getParentFile().mkdirs();
        file.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[content.available()];
        content.read(buffer);
        outputStream.write(buffer);
        outputStream.close();

        log.atInfo().log("Сохранил картинку на сервер");
        String resultPath = DefaultSettings.FILE_ACCESS_URL_PATH + shortPath;
        return new UuidAndLoginAndPath(uuid, userLoginFromSession, resultPath);
    }

    public static String getPathAfterWebapp(String filePrefix) {
        return filePrefix.split("webapp")[1];
    }
}
