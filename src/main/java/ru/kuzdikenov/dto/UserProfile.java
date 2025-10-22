package ru.kuzdikenov.dto;

import ru.kuzdikenov.entity.Initiative;
import ru.kuzdikenov.entity.UserRole;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class UserProfile {
    private String login;
    private String name;
    private String profilePicture;
    private String registrationDate;
    private int initiativesCount;
    private int completedProjects;
    private List<Initiative> initiatives;

    public UserProfile(String login, String name, String profilePicture, String registrationDate, int initiativesCount, int completedProjects, List<Initiative> initiatives) {
        this.login = login;
        this.name = name;
        this.profilePicture = profilePicture;
        this.registrationDate = registrationDate;
        this.initiativesCount = initiativesCount;
        this.completedProjects = completedProjects;
        this.initiatives = initiatives;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public int getInitiativesCount() {
        return initiativesCount;
    }

    public int getCompletedProjects() {
        return completedProjects;
    }

    public List<Initiative> getInitiatives() {
        return initiatives;
    }
}
