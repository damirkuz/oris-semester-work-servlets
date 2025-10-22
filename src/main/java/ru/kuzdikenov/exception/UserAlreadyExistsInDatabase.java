package ru.kuzdikenov.exception;

public class UserAlreadyExistsInDatabase extends Exception {
    public UserAlreadyExistsInDatabase(String message) {
        super(message);
    }
    public UserAlreadyExistsInDatabase() {
        super("Пользователь уже существует");
    }
}
