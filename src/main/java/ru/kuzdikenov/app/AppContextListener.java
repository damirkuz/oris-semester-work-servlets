package ru.kuzdikenov.app;


import ru.kuzdikenov.repository.*;
import ru.kuzdikenov.repository.impl.*;
import ru.kuzdikenov.service.CommentService;
import ru.kuzdikenov.service.ImageService;
import ru.kuzdikenov.service.InitiativeService;
import ru.kuzdikenov.service.UserService;
import ru.kuzdikenov.service.impl.CommentServiceImpl;
import ru.kuzdikenov.service.impl.ImageServiceImpl;
import ru.kuzdikenov.service.impl.InitiativeServiceImpl;
import ru.kuzdikenov.service.impl.UserServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        UserRepository userRepository = new UserRepositoryImpl();
        ImageRepository imageRepository = new ImageRepositoryImpl();
        InitiativeRepository initiativeRepository = new InitiativeRepositoryImpl();
        CommentRepository commentRepository = new CommentRepositoryImpl();
        LikeRepository likeRepository = new LikeRepositoryImpl();

        UserService UserService = new UserServiceImpl(userRepository, imageRepository);
        ImageService imageService = new ImageServiceImpl(imageRepository, userRepository);
        InitiativeService initiativeService = new InitiativeServiceImpl(initiativeRepository, userRepository, imageRepository, commentRepository, likeRepository);
        CommentService commentService = new CommentServiceImpl(commentRepository);

        sce.getServletContext().setAttribute("userService", UserService);
        sce.getServletContext().setAttribute("imageService", imageService);
        sce.getServletContext().setAttribute("initiativeService", initiativeService);
        sce.getServletContext().setAttribute("commentService", commentService);
    }


}
