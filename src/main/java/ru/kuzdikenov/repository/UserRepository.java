package ru.kuzdikenov.repository;

import ru.kuzdikenov.entity.User;
import ru.kuzdikenov.entity.UserRole;
import ru.kuzdikenov.exception.UserAlreadyExistsInDatabase;
import ru.kuzdikenov.exception.UserNotFoundInDatabase;

import java.util.UUID;

public interface UserRepository {
    void save(User user) throws UserAlreadyExistsInDatabase;
    User getByLogin(String login) throws UserNotFoundInDatabase;
    void changePasswordHash(User user, String passwordHash);
    void changeName(User user, String newName);
    void changeProfilePicture(User user, UUID imageId);
    void changeUserRole(User user, UserRole userRole);
    void delete(User user);
    // TODO: метод, получающий данные для статистики личного кабинета пользователя
}
