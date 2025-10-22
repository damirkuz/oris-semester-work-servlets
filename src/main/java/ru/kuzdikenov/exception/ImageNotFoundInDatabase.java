package ru.kuzdikenov.exception;

public class ImageNotFoundInDatabase extends Exception {
    public ImageNotFoundInDatabase(String message) {
        super(message);
    }
    public ImageNotFoundInDatabase() {super("Изображение не найдено");}
}

