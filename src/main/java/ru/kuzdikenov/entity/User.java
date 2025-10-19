package ru.kuzdikenov.entity;

import java.time.Instant;
import java.util.UUID;

public class User {
    private int id;
    private String login;
    private String passwordHash;
    private String name;
    private UUID profilePictureId;
    private UserRole userRole;
    private Instant createdAt;

    public User(String name, String login, String passwordHash) {
        this.name = name;
        this.login = login;
        this.passwordHash = passwordHash;
        this.userRole = UserRole.USER;
    }

    public User(int id, String login, String passwordHash, String name, UUID profilePictureId, UserRole userRole, Instant createdAt) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
        this.name = name;
        this.profilePictureId = profilePictureId;
        this.userRole = userRole;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getName() {
        return name;
    }

    public UUID getProfilePictureId() {
        return profilePictureId;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
