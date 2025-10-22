package ru.kuzdikenov.entity;

import java.time.Instant;
import java.util.UUID;

public class Image {
    private UUID id;
    private int uploaderUserId;
    private String path;
    private Instant createdAt;

    public Image(UUID id, int uploaderUserId, String path, Instant createdAt) {
        this.id = id;
        this.uploaderUserId = uploaderUserId;
        this.path = path;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public int getUploaderUserId() {
        return uploaderUserId;
    }

    public String getPath() {
        return path;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
