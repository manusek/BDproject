package com.example.projektbd;

public class User {
    private String username;
    private int userID;

    public User(String username, int userID) {
        this.username = username;
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
