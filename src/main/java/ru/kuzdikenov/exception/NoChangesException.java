package ru.kuzdikenov.exception;

import ru.kuzdikenov.helper.LoginPasswordUtil;

public class NoChangesException extends Exception {
    public NoChangesException(String message) {
        super(message);
    }
    public NoChangesException() {
        super();
    }
}
