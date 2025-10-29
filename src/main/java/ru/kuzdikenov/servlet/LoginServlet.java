package ru.kuzdikenov.servlet;


import ru.kuzdikenov.app.DefaultSettings;
import ru.kuzdikenov.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "Login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    public static UserService userService;

    @Override
    public void init() {
        this.userService = (UserService) getServletContext().getAttribute("userService");
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.sendRedirect(req.getContextPath() + "/login.ftl");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        if (userService.loginPassCheck(login, password)) {
            // logic to authenticate user
            // make session
            HttpSession httpSession = req.getSession();
            httpSession.setAttribute("userLoggedIn", true);
            httpSession.setAttribute("login", login);
            req.setAttribute("userLoggedIn", true);
            req.setAttribute("userLogin", login);

            httpSession.setMaxInactiveInterval(DefaultSettings.httpSessionMaxInactiveInterval);

            System.out.println(req.getContextPath());
            resp.sendRedirect(req.getContextPath() + "/");
        } else {
            req.setAttribute("error", "Неправильный логин или пароль");
            req.getRequestDispatcher("/login.ftl").forward(req, resp);
        }
    }
}
