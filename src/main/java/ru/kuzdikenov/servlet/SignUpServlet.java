package ru.kuzdikenov.servlet;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.exception.InvalidLoginException;
import ru.kuzdikenov.exception.InvalidPasswordException;
import ru.kuzdikenov.exception.UserAlreadyExistsInDatabaseException;
import ru.kuzdikenov.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SignUp", urlPatterns = "/signUp")
public class  SignUpServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(SignUpServlet.class);
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
        } catch (InvalidLoginException | InvalidPasswordException | UserAlreadyExistsInDatabaseException e) {
            log.atError().log(e.getMessage());
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/signUp.ftl").forward(req, resp);
        }
    }
}
