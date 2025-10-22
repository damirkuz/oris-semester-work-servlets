package ru.kuzdikenov.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.entity.User;
import ru.kuzdikenov.entity.UserRole;
import ru.kuzdikenov.exception.UserAlreadyExistsInDatabase;
import ru.kuzdikenov.exception.UserNotFoundInDatabase;
import ru.kuzdikenov.helper.DatabaseConnectionUtil;
import ru.kuzdikenov.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private final Connection connection = DatabaseConnectionUtil.getConnection();

    @Override
    public void save(User user) throws UserAlreadyExistsInDatabase {
        log.atInfo().log("Сохраняю пользователя с логином" + user.getLogin());
        String sql = "insert into forum.users (name, login, password_hash, profile_picture_id, user_role) values (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getPasswordHash());
            preparedStatement.setObject(4, user.getProfilePictureId());
            preparedStatement.setString(5, user.getUserRole().getValue());

            preparedStatement.execute();
            log.atInfo().log("Пользователь с логином" + user.getLogin() + " сохранён");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserAlreadyExistsInDatabase();
        }
    }

    @Override
    public User getByLogin(String login) throws UserNotFoundInDatabase {
        log.atInfo().log("Получаем пользователя по логину");
        String sql = "SELECT * FROM forum.users WHERE login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getUserFromResultSet(rs);
                } else {
                    throw new UserNotFoundInDatabase("Пользователь с логином " + login + " не найден");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске пользователя по логину: " + login, e);
        }
    }

    private void updateOneColumnValue(User user, String columnName, Object value, boolean isStringParameter) {
        String sql = "UPDATE forum.users SET " + columnName + " = ? WHERE login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            if (isStringParameter) {
                ps.setString(1, (String) value);
            } else {
                ps.setObject(1, value);
            }

            ps.setString(2, user.getLogin());
            ps.execute();
        } catch (SQLException e) {
            log.atError().log(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changePasswordHash(User user, String passwordHash) {
        log.atInfo().log("Меняем пароль пользователя");
        updateOneColumnValue(user, "password_hash", passwordHash, true);
        log.atInfo().log("Пароль пользователя с логином" + user.getLogin() + " изменён");
    }

    @Override
    public void changeName(User user, String newName) {
        log.atInfo().log("Меняем имя пользователя с " + user.getName() + " на " + newName);
        updateOneColumnValue(user, "name", newName, true);
        log.atInfo().log("Имя пользователя с логином" + user.getLogin() + " изменено");
    }

    @Override
    public void changeProfilePicture(User user, UUID imageId) {
        log.atInfo().log("Меняем аватарку пользователя на " + imageId);
        updateOneColumnValue(user, "profile_picture_id", imageId, false);
        log.atInfo().log("Аватарка пользователя с логином " + user.getLogin() + " изменена");
    }

    @Override
    public void changeUserRole(User user, UserRole userRole) {
        log.atInfo().log("Меняем роль пользователя с " + user.getUserRole() + " на " + userRole.getValue());
        updateOneColumnValue(user, "user_role", userRole.getValue(), true);
        log.atInfo().log("Роль пользователя с логином" + user.getLogin() + " изменена");
    }

    @Override
    public void delete(User user) {
        String sql = "DELETE FROM forum.users WHERE login = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getLogin());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                log.atInfo().log("Пользователь " + user.getLogin() + " удалён");
            } else {
                log.atError().log("Пользователь " + user.getLogin() + "не был удалён");
                throw new RuntimeException();
            }

        } catch (SQLException e) {
            log.atError().log(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User(
                resultSet.getInt("id"),
                resultSet.getString("login"),
                resultSet.getString("password_hash"),
                resultSet.getString("name"),
                (UUID) resultSet.getObject("profile_picture_id"),
                 UserRole.valueOf(resultSet.getString("user_role")),
                 resultSet.getTimestamp("created_at").toInstant()
        );
        return user;
    }
}
