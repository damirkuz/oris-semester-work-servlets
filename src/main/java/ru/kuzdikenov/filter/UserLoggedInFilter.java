package ru.kuzdikenov.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class UserLoggedInFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession httpSession = req.getSession();
        Boolean userLoggedIn = (Boolean) httpSession.getAttribute("userLoggedIn");
        if (userLoggedIn != null && userLoggedIn) {
            req.setAttribute("userLoggedIn", true);
            req.setAttribute("userLogin", httpSession.getAttribute("login"));
        } else {
            req.setAttribute("userLoggedIn", false);
        }

        chain.doFilter(req, res);
    }
}
