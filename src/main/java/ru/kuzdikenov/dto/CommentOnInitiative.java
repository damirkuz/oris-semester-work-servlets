package ru.kuzdikenov.dto;

import ru.kuzdikenov.entity.Initiative;

import java.util.List;

public class CommentOnInitiative {
    private int id;
    private String author;
    private String text;
    private boolean ownedByMe;

    public CommentOnInitiative(int id, String author, String text, boolean ownedByMe) {
        this.id = id;
        this.author = author;
        this.text = text;
        this.ownedByMe = ownedByMe;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public boolean isOwnedByMe() {
        return ownedByMe;
    }
}
