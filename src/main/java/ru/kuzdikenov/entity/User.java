package ru.kuzdikenov.entity;

import ru.kuzdikenov.exception.UserRole;

import java.time.Instant;

public class User {
    private int id;
    private String login;
    private String password_hash;
    private String name;
    private String profilePictureId;
    private UserRole userRole;
    private Instant createdAt;
}
