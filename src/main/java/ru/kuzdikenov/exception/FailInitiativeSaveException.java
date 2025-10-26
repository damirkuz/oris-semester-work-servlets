package ru.kuzdikenov.exception;

public class FailInitiativeSaveException extends Exception {
    public FailInitiativeSaveException(String message) {
        super(message);
    }
    public FailInitiativeSaveException() {super("Ошибка при сохранении инициативы");}
}
