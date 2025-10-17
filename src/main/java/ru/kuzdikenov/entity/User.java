package ru.kuzdikenov.entity;

import java.time.Instant;

public class User {
    private int id;
    private String login;
    private String password_hash;
    private String name;
    private String profilePictureId;
    private UserRole userRole;
    private Instant createdAt;

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public String getName() {
        return name;
    }

    public String getProfilePictureId() {
        return profilePictureId;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
