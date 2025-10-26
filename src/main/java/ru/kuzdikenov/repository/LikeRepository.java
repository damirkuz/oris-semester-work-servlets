package ru.kuzdikenov.repository;

import ru.kuzdikenov.entity.Comment;
import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.entity.Like;
import ru.kuzdikenov.exception.CommentNotFoundInDatabaseException;
import ru.kuzdikenov.exception.LikeNotFoundInDatabaseException;

import java.util.List;

public interface LikeRepository {
    void save(Like like);
    List<Like> getAllFromInitiative(Initiative initiative);
    Like getById(int likeId) throws LikeNotFoundInDatabaseException;
    void delete(int likeId);
}
