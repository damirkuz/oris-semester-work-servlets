package ru.kuzdikenov.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.dto.UserProfile;
import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.entity.User;
import ru.kuzdikenov.exception.*;
import ru.kuzdikenov.helper.LoginPasswordUtil;
import ru.kuzdikenov.repository.ImageRepository;
import ru.kuzdikenov.repository.UserRepository;
import ru.kuzdikenov.service.UserService;

import java.util.List;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public UserServiceImpl(UserRepository userRepository, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public void signUp(String name, String login, String password) throws UserAlreadyExistsInDatabaseException, InvalidPasswordException, InvalidLoginException {
        if (!LoginPasswordUtil.isValidLogin(login)) {
            throw new InvalidLoginException();
        }
        if (!LoginPasswordUtil.isValidPassword(password)) {
            throw new InvalidPasswordException();
        }

        User user = new User(name, login, LoginPasswordUtil.encrypt(password));
        userRepository.save(user);
    }

    @Override
    public boolean loginPassCheck(String login, String password){
        User user;
        try {
            user = userRepository.getByLogin(login);
        } catch (UserNotFoundInDatabaseException e) {
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
        } catch (UserNotFoundInDatabaseException e) {
            // user isn't found -> login or pass is wrong
            return false;
        }

        return true;
    }

    @Override
    public UserProfile getUserProfile(String login) throws UserNotFoundInDatabaseException {
        User user = userRepository.getByLogin(login);

        // the registration date will be formatted with JavaScript
        String registrationDate = user.getCreatedAt().toString();
        String imagePath = null;
        if (user.getProfilePictureId() != null) {
            try {
                imagePath = imageRepository.getByUuid(user.getProfilePictureId()).getPath();
            } catch (ImageNotFoundInDatabaseException e) {
                throw new RuntimeException(e);
            }

        }

        // TODO: реализовать логику получения статистики по количеству инициатив
        int initiativesCount = 0;
        int completedProjects = 0;
        List<Initiative> initiatives = null;
        UserProfile userProfile = new UserProfile(user.getLogin(), user.getName(), imagePath, registrationDate, initiativesCount, completedProjects, initiatives);
        return userProfile;

    }

    @Override
    public void editProfile(String login, String name, String password, UUID profilePicture) throws InvalidPasswordException, UserNotFoundInDatabaseException, NoChangesException {
        int changesCount = 0;
        User user = userRepository.getByLogin(login);

        log.atInfo().log("Проверяем новое имя на совпадение со старым");
        if (!name.equals(user.getName())) {
            userRepository.changeName(user, name);
            changesCount++;
        }

        if (!password.isEmpty()) {
            if (!LoginPasswordUtil.isValidPassword(password)) {
                throw new InvalidPasswordException();
            }

            String encryptedPassword = LoginPasswordUtil.encrypt(password);
            log.atInfo().log("Проверяем новый пароль на совпадение со старым");
            if (!encryptedPassword.equals(user.getPasswordHash())) {
                userRepository.changePasswordHash(user, encryptedPassword);
                changesCount++;
            }
        }

        log.atInfo().log("Проверяем, что картинка не null");
        if (profilePicture != null) {
            userRepository.changeProfilePicture(user, profilePicture);
            changesCount++;
        }

        if (changesCount == 0) {
            throw new NoChangesException();
        }
    }

    @Override
    public User getById(int userId) throws UserNotFoundInDatabaseException {
        return userRepository.getById(userId);
    }

    @Override
    public User getByLogin(String userLogin) throws UserNotFoundInDatabaseException {
        return userRepository.getByLogin(userLogin);
    }


    private boolean isUserPassword(User user, String password) {
        return user.getPasswordHash().equals(LoginPasswordUtil.encrypt(password));
    }
}
