package ru.kuzdikenov.entity;

import java.time.Instant;
import java.util.List;

public class Initiative {
    private int initiativeId;
    private int creatorUserId;
    private String title;
    private String body;
    private InitiativeStatus status;
    private List<Image> images;
    private List<Like> likes;
    private List<Comment> comments;
    private Instant createdAt;

    public Initiative(int creatorUserId, String title, String body, InitiativeStatus status, List<Image> images, List<Like> likes, List<Comment> comments) {
        this.creatorUserId = creatorUserId;
        this.title = title;
        this.body = body;
        this.status = status;
        this.images = images;
        this.likes = likes;
        this.comments = comments;
    }

    public Initiative(int initiativeId, int creatorUserId, String title, String body, InitiativeStatus status, List<Image> images, List<Like> likes, List<Comment> comments, Instant createdAt) {
        this.initiativeId = initiativeId;
        this.creatorUserId = creatorUserId;
        this.title = title;
        this.body = body;
        this.status = status;
        this.images = images;
        this.likes = likes;
        this.comments = comments;
        this.createdAt = createdAt;
    }

    public Initiative(int initiativeId, int creatorUserId, String title, String body, InitiativeStatus status, Instant createdAt) {
        this.initiativeId = initiativeId;
        this.creatorUserId = creatorUserId;
        this.title = title;
        this.body = body;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getInitiativeId() {
        return initiativeId;
    }

    public int getCreatorUserId() {
        return creatorUserId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public InitiativeStatus getStatus() {
        return status;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }


    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }


}
