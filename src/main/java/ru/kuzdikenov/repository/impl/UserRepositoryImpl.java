package ru.kuzdikenov.repository.impl;

import ru.kuzdikenov.entity.User;
import ru.kuzdikenov.entity.UserRole;
import ru.kuzdikenov.exception.UserAlreadyExistsInDatabase;
import ru.kuzdikenov.exception.UserNotFoundInDatabase;
import ru.kuzdikenov.repository.UserRepository;
import ru.kuzdikenov.repository.dao.UserDao;

public class UserRepositoryImpl implements UserRepository {
    private final UserDao userDao;

    public UserRepositoryImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void save(User user) throws UserAlreadyExistsInDatabase {
        userDao.save(user);
    }

    @Override
    public User getByLogin(String login) throws UserNotFoundInDatabase {
        return userDao.getByLogin(login);
    }

    @Override
    public void changePasswordHash(User user, String passwordHash) {

    }

    @Override
    public void changeName(User user, String newName) {

    }

    @Override
    public void changeProfilePicture(User user, int imageId) {

    }

    @Override
    public void changeUserRole(User user, UserRole userRole) {

    }

    @Override
    public void delete(User user) {
        userDao.delete(user.getId());
    }

    @Override
    public void edit(User user) {

    }
}
