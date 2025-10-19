package ru.kuzdikenov.repository.dao.impl;

import ru.kuzdikenov.entity.User;
import ru.kuzdikenov.entity.UserRole;
import ru.kuzdikenov.exception.UserAlreadyExistsInDatabase;
import ru.kuzdikenov.exception.UserNotFoundInDatabase;
import ru.kuzdikenov.helper.DatabaseConnectionUtil;
import ru.kuzdikenov.helper.PasswordUtil;
import ru.kuzdikenov.repository.dao.UserDao;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDaoImpl implements UserDao {

    private final Connection connection = DatabaseConnectionUtil.getConnection();

    @Override
    public void save(User user) throws UserAlreadyExistsInDatabase {
        String sql = "insert into forum.users (name, login, password_hash, profile_picture_id, user_role) values (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getPasswordHash());
            preparedStatement.setObject(4, user.getProfilePictureId());
            preparedStatement.setString(5, user.getUserRole().getValue());

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserAlreadyExistsInDatabase();
        }
    }

    @Override
    public User getByLogin(String login) throws UserNotFoundInDatabase {
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

    @Override
    public User getById(int userId) {
        return null;
    }

    @Override
    public void changePasswordHash(int userId, String passwordHash) {

    }

    @Override
    public void changeName(int userId, String newName) {

    }

    @Override
    public void changeProfilePicture(int userId, int imageId) {

    }

    @Override
    public void changeUserRole(int userId, UserRole userRole) {

    }

    @Override
    public void delete(int userId) {

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
