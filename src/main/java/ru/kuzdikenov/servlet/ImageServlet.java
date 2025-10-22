package ru.kuzdikenov.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.app.DefaultSettings;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(urlPatterns = DefaultSettings.FILE_ACCESS_URL_PATH + "/*")
public class ImageServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ImageServlet.class);
    private final String basePath = DefaultSettings.FILE_STORAGE_DIR + DefaultSettings.FILE_ACCESS_URL_PATH;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // this servlet get image from disk
        String filename = req.getPathInfo(); // /44/filename.png
        if (filename == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        log.atInfo().log("Ищу изображение, лежащее в " + basePath + filename);
        File file = new File(basePath, filename);
        if (!file.exists() || file.isDirectory()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            log.atError().log("Изображение" + basePath + filename + "не найдено");
            return;
        }

        resp.setContentType(getServletContext().getMimeType(file.getName()));
        try (InputStream in = new FileInputStream(file); OutputStream out = resp.getOutputStream()) {
            in.transferTo(out);
            log.atInfo().log("Изображение " + basePath + filename + " найдено");
        }
    }
}
