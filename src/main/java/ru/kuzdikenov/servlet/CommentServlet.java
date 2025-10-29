package ru.kuzdikenov.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.entity.Comment;
import ru.kuzdikenov.entity.User;
import ru.kuzdikenov.entity.UserRole;
import ru.kuzdikenov.exception.CommentNotFoundInDatabaseException;
import ru.kuzdikenov.exception.UserNotFoundInDatabaseException;
import ru.kuzdikenov.helper.UrlUtil;
import ru.kuzdikenov.service.CommentService;
import ru.kuzdikenov.service.ImageService;
import ru.kuzdikenov.service.InitiativeService;
import ru.kuzdikenov.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Comment", urlPatterns = "/comment/*")
public class CommentServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(CommentServlet.class);

    private static CommentService commentService;
    private static UserService userService;

    @Override
    public void init() {
        commentService = (CommentService) getServletContext().getAttribute("commentService");
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.atInfo().log("Получаем id комментария из строки");
        Integer commentId = UrlUtil.parseIdOr404(req, resp);
        if (commentId == null) {return;}

        String userAction = UrlUtil.getUrlAfterSlash(req, resp, 2); // for example: edit

        String userLoginFromSession = (String) req.getSession().getAttribute("login");

        if (userLoginFromSession == null) {
            log.atError().log("Неавторизованный пользователь пытается взаимодействовать с комментарием");
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }


        Comment comment;
        try {
            comment = commentService.getById(commentId);
        } catch (CommentNotFoundInDatabaseException e) {
            log.atError().log("Комментарий " + commentId + " не найден");
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String authorComment = null;
        User requesterUser = null;
        try {
            authorComment = userService.getById(comment.getAuthorUserId()).getLogin(); // in comment always right user id
            requesterUser = userService.getByLogin(userLoginFromSession);
        } catch (UserNotFoundInDatabaseException _) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // not author and not admin
        if (!userLoginFromSession.equals(authorComment) && !requesterUser.getUserRole().equals(UserRole.ADMIN)) {
            log.atError().log("Пользователь не имеет права взаимодействовать c комментарием");
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        switch (userAction) {
            case "edit": {
                String newBody = req.getParameter("text");
                commentService.changeBody(comment, newBody);
                break;
            }
            case "delete": {
                commentService.delete(comment);
                break;
            }
            default: return;
        }



        resp.sendRedirect("/initiative/" + comment.getInitiativeId());

    }
}
