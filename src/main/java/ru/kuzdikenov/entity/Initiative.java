package ru.kuzdikenov.entity;

import java.time.Instant;

public class Initiative {
    private int initiativeId;
    private int creatorUserId;
    private String title;
    private String body;
    private InitiativeStatus status;
    private Instant createdAt;
}
