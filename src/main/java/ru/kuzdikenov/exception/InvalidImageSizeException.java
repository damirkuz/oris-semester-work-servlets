package ru.kuzdikenov.exception;

public class InvalidImageSizeException extends Exception {
    public InvalidImageSizeException(String message) {
        super(message);
    }
    public InvalidImageSizeException() {
        super();
    }
}
