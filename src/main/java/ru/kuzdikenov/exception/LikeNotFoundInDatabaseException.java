package ru.kuzdikenov.exception;

public class LikeNotFoundInDatabaseException extends Exception {
    public LikeNotFoundInDatabaseException(String message) {
        super(message);
    }
    public LikeNotFoundInDatabaseException() {super("Лайк не найден");}
}

