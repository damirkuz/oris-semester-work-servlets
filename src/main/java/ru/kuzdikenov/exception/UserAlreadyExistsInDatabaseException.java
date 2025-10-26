package ru.kuzdikenov.exception;

public class UserAlreadyExistsInDatabaseException extends Exception {
    public UserAlreadyExistsInDatabaseException(String message) {
        super(message);
    }
    public UserAlreadyExistsInDatabaseException() {
        super("Пользователь уже существует");
    }
}
