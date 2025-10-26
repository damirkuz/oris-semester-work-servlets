package ru.kuzdikenov.exception;

public class CommentNotFoundInDatabaseException extends Exception {
    public CommentNotFoundInDatabaseException(String message) {
        super(message);
    }
    public CommentNotFoundInDatabaseException() {super("Комментарий не найден");}
}

