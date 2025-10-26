package ru.kuzdikenov.exception;

public class InvalidImageNameException extends Exception {
    public InvalidImageNameException(String message) {
        super(message);
    }
    public InvalidImageNameException() {
        super();
    }
}
