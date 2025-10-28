package ru.kuzdikenov.helper;

import com.cloudinary.Cloudinary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.app.DefaultSettings;
import ru.kuzdikenov.dto.UuidAndLoginAndPath;
import ru.kuzdikenov.exception.InvalidImageExtensionException;
import ru.kuzdikenov.exception.InvalidImageNameException;
import ru.kuzdikenov.exception.InvalidImageSizeException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

@MultipartConfig
public class ImageUtil {
//    public static final String FILE_PREFIX = DefaultSettings.FILE_STORAGE_DIR + DefaultSettings.FILE_ACCESS_URL_PATH;
    public static final int DIRECTORIES_COUNT = 100;
    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
    private static final long MAX_SIZE_BYTES = 50L * 1024 * 1024; //  50 Mb

    private static final Logger log = LoggerFactory.getLogger(ImageUtil.class);
    private static final Cloudinary cloudinary = CloudinaryUtil.getInstance();

    private static String getImagePathFromUuid(UUID uuid) {
        StringBuilder sb = new StringBuilder();
        sb.append(File.separator);
        sb.append(String.valueOf(Math.abs( uuid.hashCode() % DIRECTORIES_COUNT)));
        sb.append(File.separator);
        sb.append(uuid.toString());
        return sb.toString();
    }

    public static String getPathAfterWebapp(String filePrefix) {
        return filePrefix.split("webapp")[1];
    }

    private static String contentTypeToExt(String ct) {
        if ("image/jpeg".equals(ct)) return ".jpg";
        if ("image/png".equals(ct)) return ".png";
        if ("image/webp".equals(ct)) return ".webp";
        return "";
    }

    public static List<UuidAndLoginAndPath> handlePhotosAndSaveToCloudinary(HttpServletRequest req, String parameterName)
            throws ServletException, IOException, InvalidImageExtensionException,
            InvalidImageSizeException, InvalidImageNameException {

        log.info("Получаю картинки из формы, параметр: {}", parameterName);

        List<Part> parts = extractImageParts(req, parameterName);
        log.info("Найдено файлов по параметру {}: {}", parameterName, parts.size());
        if (parts.isEmpty()) {
            log.info("Файлы не переданы — возвращаю пустой список");
            return Collections.emptyList();
        }

        String userLogin = (String) req.getSession().getAttribute("login");
        log.info("Пользователь из сессии: {}", userLogin);

        Cloudinary cloudinary = CloudinaryUtil.getInstance();
        List<UuidAndLoginAndPath> result = new ArrayList<>();

        int idx = 0;
        for (Part part : parts) {
            idx++;
            try {
                result.add(processSinglePart(cloudinary, part, userLogin, idx));
            } finally {
                safeDeletePart(part, idx);
            }
        }

        log.info("Сохранено файлов в Cloudinary: {}", result.size());
        return result;
    }

    private static List<Part> extractImageParts(HttpServletRequest req, String parameterName) throws ServletException, IOException {
        Collection<Part> all = req.getParts();
        log.info("Всего частей в запросе: {}", all.size());
        return all.stream()
                .filter(p -> parameterName.equals(p.getName()) || (parameterName + "[]").equals(p.getName()))
                .filter(p -> p.getSize() > 0)
                .toList();
    }

    private static UuidAndLoginAndPath processSinglePart(Cloudinary cloudinary, Part part, String userLogin, int idx)
            throws IOException, InvalidImageExtensionException, InvalidImageSizeException, InvalidImageNameException {

        String contentType = part.getContentType();
        long size = part.getSize();
        log.info("#{}: Получен файл: name={}, contentType={}, size={}", idx, part.getName(), contentType, size);

        validateImageMeta(contentType, size, idx);

        String submitted = part.getSubmittedFileName();
        if (submitted == null || submitted.isBlank()) {
            log.error("#{}: Отклонён: пустое имя файла", idx);
            throw new InvalidImageNameException("Пустое имя файла");
        }

        String filename = Paths.get(submitted).getFileName().toString();
        String ext = resolveExtension(filename, contentType);
        log.info("#{}: Итоговое имя={}, расширение={}", idx, filename, ext);

        UUID uuid = UUID.randomUUID();
        String publicId = uuid.toString();

        Map<String, Object> options = buildUploadOptions(userLogin, publicId);

        long started = System.nanoTime();
        byte[] data;
        try (InputStream in = part.getInputStream()) {
            data = in.readAllBytes();
        }

        Map<String, Object> res = cloudinary.uploader().upload(data, options);
        String url = (String) (res.get("secure_url") != null ? res.get("secure_url") : res.get("url"));

        long tookMs = (System.nanoTime() - started) / 1_000_000;
        log.info("#{}: Загрузка в Cloudinary завершена за {} мс, public_id={}, url={}", idx, tookMs, publicId, url);

        return new UuidAndLoginAndPath(uuid, userLogin, url);
    }

    private static void validateImageMeta(String contentType, long size, int idx)
            throws InvalidImageExtensionException, InvalidImageSizeException {

        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            log.error("#{}: Отклонён: неподдерживаемый формат: {}", idx, contentType);
            throw new InvalidImageExtensionException("Неподдерживаемый формат: " + contentType);
        }
        if (size > MAX_SIZE_BYTES) {
            log.error("#{}: Отклонён: размер {} > лимит {}", idx, size, MAX_SIZE_BYTES);
            throw new InvalidImageSizeException("Файл превышает допустимый размер " + MAX_SIZE_BYTES + " байт");
        }
    }

    private static String resolveExtension(String filename, String contentType) {
        int dot = filename.lastIndexOf('.');
        return (dot >= 0 && dot < filename.length() - 1)
                ? filename.substring(dot)
                : contentTypeToExt(contentType);
    }

    private static Map<String, Object> buildUploadOptions(String userLogin, String publicId) {
        Map<String, Object> options = new HashMap<>();
        options.put("resource_type", "image");
        options.put("folder", "semester-work-servlets-damirkuz/initiatives/" + userLogin);
        options.put("public_id", publicId);
        options.put("overwrite", true);
        options.put("secure", true);
        return options;
    }

    private static void safeDeletePart(Part part, int idx) {
        try {
            part.delete();
            log.debug("#{}: Временные ресурсы Part очищены", idx);
        } catch (Exception e) {
            log.warn("#{}: Не удалось удалить Part: {}", idx, e.toString());
        }
    }


//    public static List<UuidAndLoginAndPath> handlePhotosAndSaveToServer(HttpServletRequest req, String parameterName) throws ServletException, IOException, InvalidImageExtensionException, InvalidImageSizeException, InvalidImageNameException {
//        log.atInfo().log("Получаю картинки из формы, параметр: %s", parameterName);
//
//        Collection<Part> parts = req.getParts();
//        log.atInfo().log("Всего частей в запросе: %d", parts.size());
//
//
//        List<Part> partList = parts.stream()
//                .filter(part -> {
//                    String name = part.getName();
//                    return parameterName.equals(name) || (parameterName + "[]").equals(name);
//                })
//                .filter(part -> part.getSize() > 0)
//                .toList();
//
//        log.atInfo().log("Отфильтровано файлов с именем %s/[] и ненулевым размером: %d", parameterName, partList.size());
//
//        if (partList.isEmpty()) {
//            log.atInfo().log("Файлы не переданы, возвращаю пустой список");
//            return Collections.emptyList();
//        }
//
//        String userLoginFromSession = (String) req.getSession().getAttribute("login");
//        log.atInfo().log("Пользователь из сессии: %s", userLoginFromSession);
//
//        List<UuidAndLoginAndPath> result = new ArrayList<>();
//
//        int index = 0;
//        for (Part part : partList) {
//            index++;
//            String contentType = part.getContentType();
//            long size = part.getSize();
//
//            log.atInfo().log("#%d: Получен файл: name=%s, contentType=%s, size=%d", index, part.getName(), contentType, size);
//
//            if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
//                log.atError().log("#%d: Отклонён: неподдерживаемый формат: %s", index, contentType);
//                part.delete();
//                throw new InvalidImageExtensionException("Неподдерживаемый формат: " + contentType);
//            }
//            if (size > MAX_SIZE_BYTES) {
//                log.atError().log("#%d: Отклонён: размер %d > лимит %d", index, size, MAX_SIZE_BYTES);
//                part.delete();
//                throw new InvalidImageSizeException("Файл превышает допустимый размер " + MAX_SIZE_BYTES + " байт");
//            }
//
//            String submitted = part.getSubmittedFileName();
//            log.atInfo().log("#%d: submittedFileName=%s", index, submitted);
//
//            if (submitted == null || submitted.isBlank()) {
//                log.atError().log("#%d: Отклонён: пустое имя файла", index);
//                part.delete();
//                throw new InvalidImageNameException("Пустое имя файла");
//            }
//
//            String filename = Paths.get(submitted).getFileName().toString();
//            int dot = filename.lastIndexOf('.');
//            String ext = (dot >= 0 && dot < filename.length() - 1) ? filename.substring(dot) : contentTypeToExt(contentType);
//
//            log.atInfo().log("#%d: Итоговое имя=%s, расширение=%s", index, filename, ext);
//
//            UUID uuid = UUID.randomUUID();
//            String shortPath = getImagePathFromUuid(uuid) + ext;
//            String path = FILE_PREFIX + shortPath;
//
//            File file = new File(path);
//            File parent = file.getParentFile();
//            if (parent != null && !parent.exists()) {
//                boolean mk = parent.mkdirs();
//                log.atInfo().log("#%d: Создание директорий %s: %s", index, parent, mk ? "успех" : "ошибка");
//                if (!mk) {
//                    part.delete();
//                    throw new IOException("Не удалось создать директории: " + parent);
//                }
//            }
//
//            long started = System.nanoTime();
//            try (InputStream in = part.getInputStream();
//                 OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
//
//                byte[] buf = new byte[64 * 1024];
//                int r;
//                long totalWritten = 0L;
//                while ((r = in.read(buf)) != -1) {
//                    out.write(buf, 0, r);
//                    totalWritten += r;
//                }
//                out.flush();
//                long tookMs = (System.nanoTime() - started) / 1_000_000;
//                log.atInfo().log("#%d: Запись завершена: %d байт в %d мс, путь=%s", index, totalWritten, tookMs, path);
//
//            } catch (IOException exception) {
//                log.atError().log("#%d: Ошибка записи файла, удаляю %s", index, path);
//                try { file.delete(); } catch (Exception ignore) {}
//                part.delete();
//                throw exception;
//            }
//
//            part.delete();
//            log.atInfo().log("#%d: Временные ресурсы Part очищены", index);
//
//            String resultPath = DefaultSettings.FILE_ACCESS_URL_PATH + shortPath;
//            result.add(new UuidAndLoginAndPath(uuid, userLoginFromSession, resultPath));
//            log.atInfo().log("#%d: Сформирован результат: uuid=%s, url=%s", index, uuid, resultPath);
//        }
//
//        log.atInfo().log("Сохранено файлов: %d", result.size());
//        return result;
//    }

}
