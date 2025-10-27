package ru.kuzdikenov.repository.impl;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.entity.Comment;
import ru.kuzdikenov.entity.Image;
import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.exception.CommentNotFoundInDatabaseException;
import ru.kuzdikenov.exception.ImageNotFoundInDatabaseException;
import ru.kuzdikenov.helper.DatabaseUtil;
import ru.kuzdikenov.repository.CommentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommentRepositoryImpl implements CommentRepository {
    private static final Logger log = LoggerFactory.getLogger(CommentRepositoryImpl.class);
    private final HikariDataSource dataSource = DatabaseUtil.createDataSource();


    @Override
    public void save(Comment comment) {

    }

    @Override
    public List<Comment> getAllFromInitiative(Initiative initiative) {
        int initiativeId = initiative.getInitiativeId();
        log.atInfo().log("Ищу комментарии инициативы " + initiativeId);

        String sql = "SELECT * FROM forum.comment WHERE initiative_id = ?";

        List<Comment> comments = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, initiativeId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    try {
                        int commentId = rs.getInt("id");
                        comments.add(getById(commentId));
                    } catch (CommentNotFoundInDatabaseException _) {}
                }
            }

        } catch (SQLException e) {
            log.atError().log("Ошибка при поиске получении изображений инициативы: " + initiativeId);
            throw new RuntimeException(e);
        }
        return comments;
    }

    @Override
    public void delete(Comment comment) {

    }

    @Override
    public Comment getById(int commentId) throws CommentNotFoundInDatabaseException {
        log.atInfo().log("Получаем комментарий по id: " + commentId);
        String sql = "SELECT * FROM forum.comment WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, commentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getCommentFromResultSet(rs);
                } else {
                    throw new CommentNotFoundInDatabaseException("Комментарий по id " + commentId + " не найден");
                }
            }
        } catch (SQLException e) {
            log.atError().log("Ошибка при поиске комментария по id: " + commentId);
            throw new RuntimeException(e);
        }
    }

    private Comment getCommentFromResultSet(ResultSet resultSet) throws SQLException {
        return new Comment(
                resultSet.getInt("id"),
                resultSet.getInt("author_user_id"),
                resultSet.getInt("initiative_id"),
                resultSet.getString("body"),
                resultSet.getTimestamp("created_at").toInstant()
        );
    }

    @Override
    public void changeBody(Comment comment, String newBody) {

    }

    @Override
    public void delete(int commentId) {

    }
}
