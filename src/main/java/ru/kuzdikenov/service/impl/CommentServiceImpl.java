package ru.kuzdikenov.service.impl;

import ru.kuzdikenov.entity.Comment;
import ru.kuzdikenov.exception.CommentNotFoundInDatabaseException;
import ru.kuzdikenov.repository.CommentRepository;
import ru.kuzdikenov.service.CommentService;

public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


    @Override
    public Comment getById(int commentId) throws CommentNotFoundInDatabaseException {
        return commentRepository.getById(commentId);
    }

    @Override
    public void changeBody(Comment comment, String newBody) {
        commentRepository.changeBody(comment.getId(), newBody);
    }

    @Override
    public void delete(Comment comment) {
        commentRepository.delete(comment.getId());
    }
}
