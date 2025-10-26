package ru.kuzdikenov.repository;

import ru.kuzdikenov.entity.User;
import ru.kuzdikenov.entity.UserRole;
import ru.kuzdikenov.exception.UserAlreadyExistsInDatabaseException;
import ru.kuzdikenov.exception.UserNotFoundInDatabaseException;

import java.util.UUID;

public interface UserRepository {
    void save(User user) throws UserAlreadyExistsInDatabaseException;
    User getByLogin(String login) throws UserNotFoundInDatabaseException;
    User getById(int userId) throws UserNotFoundInDatabaseException;
    void changePasswordHash(User user, String passwordHash);
    void changeName(User user, String newName);
    void changeProfilePicture(User user, UUID imageId);
    void changeUserRole(User user, UserRole userRole);
    void delete(User user);
    // TODO: метод, получающий данные для статистики личного кабинета пользователя
}
