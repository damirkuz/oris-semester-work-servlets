package ru.kuzdikenov.entity;

public enum InitiativeStatus {
    SUGGESTED ("SUGGESTED"),
    APPROVED ("APPROVED"),
    REJECTED ("REJECTED"),
    IN_PROGRESS ("IN_PROGRESS"),
    COMPLETED ("COMPLETED");

    private String value;

    public String getValue() {
        return value;
    }

    InitiativeStatus(String value) {
        this.value = value;
    }
}
