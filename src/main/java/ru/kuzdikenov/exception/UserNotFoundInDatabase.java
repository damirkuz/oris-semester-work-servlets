package ru.kuzdikenov.exception;

public class UserNotFoundInDatabase extends Exception {
    public UserNotFoundInDatabase(String message) {
        super(message);
    }
    public UserNotFoundInDatabase() {super("Пользователь не найден");}
}

