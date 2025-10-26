package ru.kuzdikenov.exception;

public class UserNotFoundInDatabaseException extends Exception {
    public UserNotFoundInDatabaseException(String message) {
        super(message);
    }
    public UserNotFoundInDatabaseException() {super("Пользователь не найден");}
}

