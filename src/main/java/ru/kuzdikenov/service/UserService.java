package ru.kuzdikenov.service;

import ru.kuzdikenov.dto.UserProfile;
import ru.kuzdikenov.entity.User;
import ru.kuzdikenov.exception.*;

import java.util.UUID;

public interface UserService {
    void signUp(String name, String login, String password) throws UserAlreadyExistsInDatabaseException, InvalidPasswordException, InvalidLoginException;
    boolean loginPassCheck(String login, String password);
    boolean loginExistsCheck(String login);
    UserProfile getUserProfile(String login) throws UserNotFoundInDatabaseException;
    void editProfile(String login, String name, String password, UUID profilePicture) throws InvalidPasswordException, UserNotFoundInDatabaseException, NoChangesException;
    User getById(int userId) throws UserNotFoundInDatabaseException;
    User getByLogin(String userLogin) throws UserNotFoundInDatabaseException;
}
