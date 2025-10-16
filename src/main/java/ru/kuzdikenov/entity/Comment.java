package ru.kuzdikenov.entity;

import java.time.Instant;

public class Comment {
    private int id;
    private int writerUserId;
    private int initiativeId;
    private String message;
    private Instant createdAt;
}
