package ru.kuzdikenov.exception;

import ru.kuzdikenov.helper.LoginPasswordUtil;

public class InvalidLoginException extends Exception {
    public InvalidLoginException(String message) {
        super(message);
    }
    public InvalidLoginException() {
        super("Логин не должен содержать некорректных символов. Длина должна быть от " + LoginPasswordUtil.MIN_LOGIN_LENGTH + " до " + LoginPasswordUtil.MAX_LOGIN_LENGTH + " символов");
    }
}
