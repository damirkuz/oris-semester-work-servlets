package ru.kuzdikenov.exception;

public class InvalidInitiativeTitleException extends Exception{
    public InvalidInitiativeTitleException(String message) {
        super(message);
    }
    public InvalidInitiativeTitleException() {
        super();
    }
}
