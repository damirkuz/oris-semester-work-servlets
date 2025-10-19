package ru.kuzdikenov.entity;

import java.time.Instant;
import java.util.UUID;

public class Image {
    private UUID id;
    private int uploaderUserId;
    private Instant createdAt;

    public UUID getId() {
        return id;
    }

    public int getUploaderUserId() {
        return uploaderUserId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
