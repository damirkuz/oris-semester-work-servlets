package ru.kuzdikenov.entity;

public class Like {
    private int id;
    private int userId;
    private int initiativeId;

    public Like(int id, int userId, int initiativeId) {
        this.id = id;
        this.userId = userId;
        this.initiativeId = initiativeId;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getInitiativeId() {
        return initiativeId;
    }
}
