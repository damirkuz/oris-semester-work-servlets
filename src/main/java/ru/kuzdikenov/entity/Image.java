package ru.kuzdikenov.entity;

import java.time.Instant;

public class Image {
    private String id;
    private int uploaderUserId;
    private Instant createdAt;

    public String getId() {
        return id;
    }

    public int getUploaderUserId() {
        return uploaderUserId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
