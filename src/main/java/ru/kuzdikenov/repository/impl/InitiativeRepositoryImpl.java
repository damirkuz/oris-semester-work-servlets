package ru.kuzdikenov.repository.impl;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuzdikenov.entity.*;
import ru.kuzdikenov.exception.FailInitiativeSaveException;
import ru.kuzdikenov.exception.InitiativeNotFoundInDatabaseException;
import ru.kuzdikenov.helper.DatabaseUtil;
import ru.kuzdikenov.repository.InitiativeRepository;

import java.sql.*;
import java.time.Instant;
import java.util.List;

public class InitiativeRepositoryImpl implements InitiativeRepository {
    private static final Logger log = LoggerFactory.getLogger(InitiativeRepositoryImpl.class);
    private final HikariDataSource dataSource = DatabaseUtil.createDataSource();


    @Override
    public int save(Initiative initiative) throws FailInitiativeSaveException {
        log.atInfo().log("Сохраняю инициативу: " + initiative.getTitle());
        String sql = "insert into forum.initiative (creator_user_id, title, body, status) values (?, ?, ?, ?)";

        try {
            return DatabaseUtil.withTransaction(dataSource, connection -> {
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                    preparedStatement.setInt(1, initiative.getCreatorUserId());
                    preparedStatement.setString(2, initiative.getTitle());
                    preparedStatement.setString(3, initiative.getBody());
                    preparedStatement.setString(4, initiative.getStatus().getValue());
                    preparedStatement.execute();

                    ResultSet rs = preparedStatement.getGeneratedKeys();
                    rs.next();
                    int id = rs.getInt("id");

                    if (!initiative.getImages().isEmpty()) {
                        setImages(connection, id, initiative.getImages());
                    }

                    log.atInfo().log("Инициатива " + id + " сохранена");
                    return id;
                }
            });
        } catch (SQLException e) {
            log.atError().log(e.getMessage());
            throw new FailInitiativeSaveException();
        }
    }

    private void setImages(Connection connection, int initiativeId, List<Image> images) throws SQLException {
        log.atInfo().log("Прикрепляю изображения к инициативе " + initiativeId);
        String sql = "insert into forum.image_initiative (image_id, initiative_id) values (?, ?)";


        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int count = 0;
            int batchSize = 100;
            for (Image image: images) {
                preparedStatement.setObject(1, image.getId());
                preparedStatement.setInt(2, initiativeId);
                preparedStatement.addBatch();
                if (++count % batchSize == 0) {
                    preparedStatement.execute();
                }
            }
            preparedStatement.execute();
        }
    }

    @Override
    public List<Initiative> getAllFromUser(User user) {
        return List.of();
    }

    @Override
    public void delete(Initiative initiative) {

    }

    @Override
    public Initiative getById(int initiativeId) throws InitiativeNotFoundInDatabaseException {
        String sql = "SELECT * FROM forum.initiative WHERE id = ?";

        try {
            return DatabaseUtil.withTransaction(dataSource, connection -> {
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                    preparedStatement.setInt(1, initiativeId);

                    preparedStatement.execute();

                    ResultSet rs = preparedStatement.getGeneratedKeys();
                    rs.next();


                    int id = rs.getInt("id");
                    int creatorUserId = rs.getInt("creator_user_id");
                    String title = rs.getString("title");
                    String body = rs.getString("body");
                    InitiativeStatus status = InitiativeStatus.valueOf(rs.getString("status"));
                    Instant createdAt = rs.getTimestamp("created_at").toInstant();

                    log.atInfo().log("Инициатива (без прикреплений) " + id + " успешно извлечена из бд");
                    return new Initiative(id, creatorUserId, title, body, status, createdAt);
                }
            });
        } catch (SQLException e) {
            log.atError().log(e.getMessage());
            throw new InitiativeNotFoundInDatabaseException();
        }

    }

    @Override
    public void changeTitle(Initiative initiative, String newTitle) {

    }

    @Override
    public void changeBody(Initiative initiative, String newBody) {

    }

    @Override
    public void changeStatus(Initiative initiative, InitiativeStatus initiativeStatus) {

    }


}
