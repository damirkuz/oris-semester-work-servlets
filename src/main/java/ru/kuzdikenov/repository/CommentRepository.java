package ru.kuzdikenov.repository;

import ru.kuzdikenov.entity.Comment;
import ru.kuzdikenov.entity.Initiative;

import java.util.List;

public interface CommentRepository {
    void save(Comment comment);
    List<Comment> getAllFromInitiative(Initiative initiative);
    void edit(Comment comment);
    void delete(int commentId);
}
