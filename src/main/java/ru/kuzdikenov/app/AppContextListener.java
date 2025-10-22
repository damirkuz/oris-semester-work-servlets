package ru.kuzdikenov.app;


import ru.kuzdikenov.repository.ImageRepository;
import ru.kuzdikenov.repository.UserRepository;
import ru.kuzdikenov.repository.impl.ImageRepositoryImpl;
import ru.kuzdikenov.repository.impl.UserRepositoryImpl;
import ru.kuzdikenov.service.ImageService;
import ru.kuzdikenov.service.UserService;
import ru.kuzdikenov.service.impl.ImageServiceImpl;
import ru.kuzdikenov.service.impl.UserServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        // TODO: написать зависимости
        UserRepository userRepository = new UserRepositoryImpl();
        ImageRepository imageRepository = new ImageRepositoryImpl();
        UserService UserService = new UserServiceImpl(userRepository, imageRepository);
        ImageService imageService = new ImageServiceImpl(imageRepository, userRepository);
        sce.getServletContext().setAttribute("userService", UserService);
        sce.getServletContext().setAttribute("imageService", imageService);
    }


}
