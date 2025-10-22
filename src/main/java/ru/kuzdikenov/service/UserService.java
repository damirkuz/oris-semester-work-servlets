package ru.kuzdikenov.service;

import ru.kuzdikenov.dto.UserProfile;
import ru.kuzdikenov.exception.*;

import java.util.UUID;

public interface UserService {
    void signUp(String name, String login, String password) throws UserAlreadyExistsInDatabase, InvalidPasswordException, InvalidLoginException;
    boolean loginPassCheck(String login, String password);
    boolean loginExistsCheck(String login);
    UserProfile getUserProfile(String login) throws UserNotFoundInDatabase;
    void editProfile(String login, String name, String password, UUID profilePicture) throws InvalidPasswordException, UserNotFoundInDatabase, NoChangesException;
}
