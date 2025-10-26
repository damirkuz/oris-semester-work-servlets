package ru.kuzdikenov.exception;

public class ImageNotFoundInDatabaseException extends Exception {
    public ImageNotFoundInDatabaseException(String message) {
        super(message);
    }
    public ImageNotFoundInDatabaseException() {super("Изображение не найдено");}
}

