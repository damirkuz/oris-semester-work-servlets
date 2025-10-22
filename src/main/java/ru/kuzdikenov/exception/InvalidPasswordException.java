package ru.kuzdikenov.exception;

import ru.kuzdikenov.helper.LoginPasswordUtil;

public class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(message);
    }
    public InvalidPasswordException() {
        super("Пароль не должен содержать некорректных символов. Длина должна быть от " + LoginPasswordUtil.MIN_PASSWORD_LENGTH + " до " + LoginPasswordUtil.MAX_PASSWORD_LENGTH + " символов");
    }
}
