package ru.kuzdikenov.repository;

import ru.kuzdikenov.entity.User;
import ru.kuzdikenov.entity.UserRole;

public interface UserRepository {
    void save(User user);
    User getByLogin(String login);
    void changePasswordHash(User user, String passwordHash);
    void changeName(User user, String newName);
    void changeProfilePicture(User user, int imageId);
    void changeUserRole(User user, UserRole userRole);
    void delete(User user);
    void edit(User user);
    // TODO: метод, получающий данные для статистики личного кабинета пользователя
}
