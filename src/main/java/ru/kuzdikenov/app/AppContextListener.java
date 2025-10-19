package ru.kuzdikenov.app;


import ru.kuzdikenov.repository.UserRepository;
import ru.kuzdikenov.repository.dao.UserDao;
import ru.kuzdikenov.repository.dao.impl.UserDaoImpl;
import ru.kuzdikenov.repository.impl.UserRepositoryImpl;
import ru.kuzdikenov.service.UserService;
import ru.kuzdikenov.service.impl.UserServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        // TODO: написать зависимости
        UserDao userDao = new UserDaoImpl();
        UserRepository userRepository = new UserRepositoryImpl(userDao);
        UserService UserService = new UserServiceImpl(userRepository);
        sce.getServletContext().setAttribute("userService", UserService);
    }


}
