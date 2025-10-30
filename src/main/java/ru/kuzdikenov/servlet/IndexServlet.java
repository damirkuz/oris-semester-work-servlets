package ru.kuzdikenov.servlet;

import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.service.ImageService;
import ru.kuzdikenov.service.InitiativeService;
import ru.kuzdikenov.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Index", urlPatterns = "")
public class IndexServlet extends HttpServlet {

    private static InitiativeService initiativeService;

    @Override
    public void init() {
        initiativeService = (InitiativeService) getServletContext().getAttribute("initiativeService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // sorted desc
        List<Initiative> initiatives = initiativeService.getAll().stream()
                .sorted((in1, in2) -> - in1.getCreatedAt().compareTo(in2.getCreatedAt())).toList();
        req.setAttribute("initiatives", initiatives);
        req.getRequestDispatcher("/index.ftl").forward(req, resp);
    }
}
