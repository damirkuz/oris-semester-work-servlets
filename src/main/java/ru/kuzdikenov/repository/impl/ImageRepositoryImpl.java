package ru.kuzdikenov.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.entity.Image;
import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.exception.ImageNotFoundInDatabase;
import ru.kuzdikenov.helper.DatabaseConnectionUtil;
import ru.kuzdikenov.repository.ImageRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ImageRepositoryImpl implements ImageRepository {
    private static final Logger log = LoggerFactory.getLogger(ImageRepositoryImpl.class);
    private final Connection connection = DatabaseConnectionUtil.getConnection();

    @Override
    public void save(UUID uuid, int uploaderId, String path) {
        log.atInfo().log("Сохраняю изображение " + uuid + " в бд");
        String sql = "insert into forum.image (id, uploader_user_id, path) values (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, uuid);
            preparedStatement.setInt(2, uploaderId);
            preparedStatement.setString(3, path);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                log.atInfo().log("Изображение " + uuid + " сохранено");
            } else {
                log.atError().log("Изображение не было сохранено " + uuid);
                throw new RuntimeException();
            }
        } catch (SQLException e) {
            log.atError().log(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Image> getAllImagesFromInitiative(Initiative initiative) {
        return List.of();
    }

    @Override
    public void delete(int imageId) {

    }

    @Override
    public Image getByUuid(UUID uuid) throws ImageNotFoundInDatabase {
        log.atInfo().log("Получаем изображение по uuid: " + uuid.toString());
        String sql = "SELECT * FROM forum.image WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, uuid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getImageFromResultSet(rs);
                } else {
                    throw new ImageNotFoundInDatabase("Изображение по uuid " + uuid + " не найдено");
                }
            }
        } catch (SQLException e) {
            log.atError().log("Ошибка при поиске изображения по uuid: " + uuid);
            throw new RuntimeException(e);
        }
    }

    private Image getImageFromResultSet(ResultSet resultSet) throws SQLException {
        return new Image(
                (UUID) resultSet.getObject("id"),
                resultSet.getInt("uploader_user_id"),
                resultSet.getString("path"),
                resultSet.getTimestamp("created_at").toInstant()
        );
    }
}
