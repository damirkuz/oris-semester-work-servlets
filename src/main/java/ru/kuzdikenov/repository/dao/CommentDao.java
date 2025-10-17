package ru.kuzdikenov.repository.dao;

import ru.kuzdikenov.entity.Comment;
import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.entity.InitiativeStatus;

import java.util.List;

public interface CommentDao {
    void save(Comment comment);
    Comment getById(int commentId);
    List<Comment> getAllFromInitiativeId(int initiativeId);
    void changeBody(int commentId, String newBody);
    void delete(int commentId);
}
