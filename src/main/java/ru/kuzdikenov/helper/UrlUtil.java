package ru.kuzdikenov.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtil {

    public static String getUrlAfterSlash(HttpServletRequest req, HttpServletResponse resp, int slashIndex) throws IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        String result;
        try {
            return path.split("/")[slashIndex];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static Integer parseIdOr404(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer initiativeId = parseId(req.getPathInfo());
        if (initiativeId == null) { resp.sendError(HttpServletResponse.SC_NOT_FOUND); return null; }
        return initiativeId;
    }


    private static Integer parseId(String path) {
        if (path == null) return null;
        Matcher m = Pattern.compile("(^|/)(\\d+)(?=/|$)").matcher(path);
        return m.find() ? Integer.valueOf(m.group(2)) : null;
    }
}
