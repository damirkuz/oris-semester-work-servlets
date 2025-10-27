package ru.kuzdikenov.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UrlUtil {

    public static String getUrlAfterSlash(HttpServletRequest req, HttpServletResponse resp, int slashIndex) throws IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return path.split("/")[slashIndex];
    }
}
