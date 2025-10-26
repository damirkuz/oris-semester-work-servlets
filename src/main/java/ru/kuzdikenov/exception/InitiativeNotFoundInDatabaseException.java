package ru.kuzdikenov.exception;

public class InitiativeNotFoundInDatabaseException extends Exception {
    public InitiativeNotFoundInDatabaseException(String message) {
        super(message);
    }
    public InitiativeNotFoundInDatabaseException() {super("Инициатива не найдена в бд");}
}

