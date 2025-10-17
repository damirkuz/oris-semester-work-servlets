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
