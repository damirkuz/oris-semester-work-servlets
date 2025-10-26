package ru.kuzdikenov.repository.impl;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.entity.Image;
import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.exception.ImageNotFoundInDatabaseException;
import ru.kuzdikenov.helper.DatabaseUtil;
import ru.kuzdikenov.repository.ImageRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImageRepositoryImpl implements ImageRepository {
    private static final Logger log = LoggerFactory.getLogger(ImageRepositoryImpl.class);
    private final HikariDataSource dataSource = DatabaseUtil.createDataSource();

    @Override
    public void save(UUID uuid, int uploaderId, String path) {
        log.atInfo().log("Сохраняю изображение " + uuid + " в бд");
        String sql = "insert into forum.image (id, uploader_user_id, path) values (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
        int initiativeId = initiative.getInitiativeId();
        log.atInfo().log("Ищу изображения инициативы " + initiativeId);

        String sql = "SELECT * FROM forum.image_initiative WHERE initiative_id = ?";

        List<Image> images = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, initiativeId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    try {
                        UUID imageId = (UUID) rs.getObject("image_id");
                        images.add(getByUuid(imageId));
                    } catch (ImageNotFoundInDatabaseException e) {}
                }
            }

        } catch (SQLException e) {
            log.atError().log("Ошибка при поиске получении изображений инициативы: " + initiativeId);
            throw new RuntimeException(e);
        }
        return images;
    }

    @Override
    public void delete(int imageId) {

    }

    @Override
    public Image getByUuid(UUID uuid) throws ImageNotFoundInDatabaseException {
        log.atInfo().log("Получаем изображение по uuid: " + uuid);
        String sql = "SELECT * FROM forum.image WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, uuid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getImageFromResultSet(rs);
                } else {
                    throw new ImageNotFoundInDatabaseException("Изображение по uuid " + uuid + " не найдено");
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
