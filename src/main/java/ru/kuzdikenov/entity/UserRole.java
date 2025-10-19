package ru.kuzdikenov.entity;

public enum UserRole {
    USER ("USER"),
    ADMIN ("ADMIN");

    private String value;

    public String getValue() {
        return value;
    }

    UserRole(String value) {
        this.value = value;
    }

    public static UserRole findByAbbr(String abbr) {
        for(UserRole v : values()){
            if(v.getValue().equals(abbr)){
                return v;
            }
        }
        // else return user
        return USER;
    }
}
