package ru.kuzdikenov.service;

import ru.kuzdikenov.entity.Comment;
import ru.kuzdikenov.entity.User;
import ru.kuzdikenov.exception.CommentNotFoundInDatabaseException;
import ru.kuzdikenov.exception.UserNotFoundInDatabaseException;

public interface CommentService {
    Comment getById(int commentId) throws CommentNotFoundInDatabaseException;
    void changeBody(Comment comment, String newBody);
    void delete(Comment comment);
}
