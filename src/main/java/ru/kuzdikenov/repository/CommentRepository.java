package ru.kuzdikenov.repository;

import ru.kuzdikenov.entity.Comment;
import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.exception.CommentNotFoundInDatabaseException;

import java.util.List;

public interface CommentRepository {
    void save(Comment comment);
    List<Comment> getAllFromInitiative(Initiative initiative);
    Comment getById(int commentId) throws CommentNotFoundInDatabaseException;
    void changeBody(int commentId, String newBody);
    void delete(int commentId);
}
