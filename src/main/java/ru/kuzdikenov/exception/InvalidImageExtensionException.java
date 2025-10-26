package ru.kuzdikenov.exception;

public class InvalidImageExtensionException extends Exception {
    public InvalidImageExtensionException(String message) {
        super(message);
    }
    public InvalidImageExtensionException() {
        super();
    }
}
