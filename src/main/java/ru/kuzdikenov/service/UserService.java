package ru.kuzdikenov.service;

import ru.kuzdikenov.entity.User;
import ru.kuzdikenov.exception.UserAlreadyExistsInDatabase;

import java.util.List;

public interface UserService {
    void signUp(String name, String login, String password) throws UserAlreadyExistsInDatabase;
    boolean loginPassCheck(String login, String password);
    boolean loginExistsCheck(String login);
}
