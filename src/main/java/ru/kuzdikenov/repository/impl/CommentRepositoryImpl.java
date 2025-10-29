package ru.kuzdikenov.repository.impl;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.entity.Comment;
import ru.kuzdikenov.entity.Image;
import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.entity.Like;
import ru.kuzdikenov.exception.CommentNotFoundInDatabaseException;
import ru.kuzdikenov.exception.ImageNotFoundInDatabaseException;
import ru.kuzdikenov.exception.LikeNotFoundInDatabaseException;
import ru.kuzdikenov.helper.DatabaseUtil;
import ru.kuzdikenov.repository.CommentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommentRepositoryImpl implements CommentRepository {
    private static final Logger log = LoggerFactory.getLogger(CommentRepositoryImpl.class);
    private final HikariDataSource dataSource = DatabaseUtil.createDataSource();


    @Override
    public void save(Comment comment) {
        log.info("Сохраняю комментарий пользователя: {} на инициативу {} с текстом {}", comment.getAuthorUserId(), comment.getInitiativeId(), comment.getBody());
        String sql = "insert into forum.comment (author_user_id, initiative_id, body) values (?, ?, ?)";

        try {
            DatabaseUtil.withTransaction(dataSource, connection -> {
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                    preparedStatement.setInt(1, comment.getAuthorUserId());
                    preparedStatement.setInt(2, comment.getInitiativeId());
                    preparedStatement.setString(3, comment.getBody());
                    preparedStatement.execute();
                    log.info("Комментарий пользователя: {} на инициативу {} с текстом {} сохранён", comment.getAuthorUserId(), comment.getInitiativeId(), comment.getBody());
                    return 0;
                }
            });
        } catch (SQLException e) {
            log.atError().log(e.getMessage());
        }
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
    public void changeBody(int commentId, String newBody) {
        log.atInfo().log("Меняю текст комментария " + commentId);

        String sql = "UPDATE forum.comment SET body = ? WHERE id = ?";


        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newBody);
            preparedStatement.setInt(2, commentId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.atError().log("Ошибка при обновлении текста комментария: " + commentId);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int commentId) {
        log.atInfo().log("Удаляю комментарий " + commentId);

        String sql = "DELETE FROM forum.comment WHERE id = ?";


        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, commentId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.atError().log("Ошибка при удалении комментария: " + commentId);
            throw new RuntimeException(e);
        }
    }
}
