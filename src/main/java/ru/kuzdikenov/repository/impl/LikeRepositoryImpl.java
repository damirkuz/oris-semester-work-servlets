package ru.kuzdikenov.repository.impl;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.entity.Comment;
import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.entity.Like;
import ru.kuzdikenov.exception.CommentNotFoundInDatabaseException;
import ru.kuzdikenov.exception.LikeNotFoundInDatabaseException;
import ru.kuzdikenov.helper.DatabaseUtil;
import ru.kuzdikenov.repository.LikeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LikeRepositoryImpl implements LikeRepository {
    private static final Logger log = LoggerFactory.getLogger(LikeRepositoryImpl.class);
    private final HikariDataSource dataSource = DatabaseUtil.createDataSource();



    @Override
    public void save(Like like) {

    }

    @Override
    public List<Like> getAllFromInitiative(Initiative initiative) {
        int initiativeId = initiative.getInitiativeId();
        log.atInfo().log("Ищу лайки инициативы " + initiativeId);

        String sql = "SELECT * FROM forum.likes WHERE initiative_id = ?";

        List<Like> likes = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, initiativeId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    try {
                        int likeId = rs.getInt("id");
                        likes.add(getById(likeId));
                    } catch (LikeNotFoundInDatabaseException _) {}
                }
            }

        } catch (SQLException e) {
            log.atError().log("Ошибка при поиске получении лайков инициативы: " + initiativeId);
            throw new RuntimeException(e);
        }
        return likes;
    }

    @Override
    public Like getById(int likeId) throws LikeNotFoundInDatabaseException{
        log.atInfo().log("Получаем лайк по id: " + likeId);
        String sql = "SELECT * FROM forum.likes WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, likeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getLikeFromResultSet(rs);
                } else {
                    throw new LikeNotFoundInDatabaseException("Лайк по id " + likeId + " не найден");
                }
            }
        } catch (SQLException e) {
            log.atError().log("Ошибка при поиске лайка по id: " + likeId);
            throw new RuntimeException(e);
        }
    }

    private Like getLikeFromResultSet(ResultSet resultSet) throws SQLException {
        return new Like(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getInt("initiative_id")
        );
    }

    @Override
    public void delete(int likeId) {

    }
}
