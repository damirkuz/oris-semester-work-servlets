package ru.kuzdikenov.service.impl;


import ru.kuzdikenov.entity.User;
import ru.kuzdikenov.exception.UserAlreadyExistsInDatabase;
import ru.kuzdikenov.exception.UserNotFoundInDatabase;
import ru.kuzdikenov.helper.PasswordUtil;
import ru.kuzdikenov.repository.UserRepository;
import ru.kuzdikenov.repository.dao.UserDao;
import ru.kuzdikenov.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void signUp(String name, String login, String password) throws UserAlreadyExistsInDatabase {
        User user = new User(name, login, PasswordUtil.encrypt(password));
        userRepository.save(user);
    }

    @Override
    public boolean loginPassCheck(String login, String password){
        User user;
        try {
            user = userRepository.getByLogin(login);
        } catch (UserNotFoundInDatabase e) {
            // user isn't found -> login or pass is wrong
            return false;
        }

        return isUserPassword(user, password);
    }

    @Override
    public boolean loginExistsCheck(String login) {
        User user;
        try {
            user = userRepository.getByLogin(login);
        } catch (UserNotFoundInDatabase e) {
            // user isn't found -> login or pass is wrong
            return false;
        }

        return true;
    }


    private boolean isUserPassword(User user, String password) {
        return user.getPasswordHash().equals(PasswordUtil.encrypt(password));
    }
}
