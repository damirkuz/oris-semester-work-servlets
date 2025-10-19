package ru.kuzdikenov.servlet;


import ru.kuzdikenov.exception.UserAlreadyExistsInDatabase;
import ru.kuzdikenov.service.UserService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SignUp", urlPatterns = "/signUp")
public class SignUpServlet extends HttpServlet {

    public static UserService userService;

    @Override
    public void init() {
        this.userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.getRequestDispatcher("/signUp.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // registration

        String name = req.getParameter("name");
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        try {
            userService.signUp(name, login, password);
            resp.sendRedirect("/login");
        } catch (UserAlreadyExistsInDatabase e) {
            req.setAttribute("error", "Пользователь уже существует");
            req.getRequestDispatcher("/signUp.ftl").forward(req, resp);
        }
    }
}
