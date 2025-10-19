package ru.kuzdikenov.repository.dao;

import ru.kuzdikenov.entity.Image;
import ru.kuzdikenov.entity.User;
import ru.kuzdikenov.entity.UserRole;
import ru.kuzdikenov.exception.UserAlreadyExistsInDatabase;
import ru.kuzdikenov.exception.UserNotFoundInDatabase;

public interface UserDao {
    void save(User user) throws UserAlreadyExistsInDatabase;
    User getById(int userId) throws UserNotFoundInDatabase;
    User getByLogin(String login) throws UserNotFoundInDatabase;
    void changePasswordHash(int userId, String passwordHash);
    void changeName(int userId, String newName);
    void changeProfilePicture(int userId, int imageId);
    void changeUserRole(int userId, UserRole userRole);
    void delete(int userId);
}
