package ru.kuzdikenov.entity;

import java.time.Instant;

public class Comment {
    private int id;
    private int authorUserId;
    private int initiativeId;
    private String body;
    private Instant createdAt;

    public int getId() {
        return id;
    }

    public int getAuthorUserId() {
        return authorUserId;
    }

    public int getInitiativeId() {
        return initiativeId;
    }

    public String getBody() {
        return body;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
