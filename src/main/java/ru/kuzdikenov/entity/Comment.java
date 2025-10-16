package ru.kuzdikenov.entity;

import java.time.Instant;

public class Comment {
    private int id;
    private int authorUserId;
    private int initiativeId;
    private String body;
    private Instant createdAt;
}
