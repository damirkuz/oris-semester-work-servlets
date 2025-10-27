package ru.kuzdikenov.repository;

import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.entity.Like;
import ru.kuzdikenov.exception.LikeNotFoundInDatabaseException;

import java.util.List;

public interface LikeRepository {
    void save(Like like);
    List<Like> getAllFromInitiative(Initiative initiative);
    Like getById(int likeId) throws LikeNotFoundInDatabaseException;
    boolean checkUserLikedInitiative(int userId, int initiativeId);
    void delete(int userId, int initiativeId);
}
