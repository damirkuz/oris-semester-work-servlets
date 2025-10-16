package ru.kuzdikenov.entity;

import ru.kuzdikenov.exception.InitiativeStatus;

import java.time.Instant;

public class Initiative {
    private int initiativeId;
    private int creatorUserId;
    private String message;
    private InitiativeStatus status;
    private Instant createdAt;
}
