package com.example.projektbd;

public class User {
    private String username;
    private static int userID;
    private static String type;

    public User(String username, int userID, String type) {
        this.username = username;
        User.userID = userID;
        User.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static int getUserID() {
        return userID;
    }

    public static void setUserID(int userID) {
        User.userID = userID;
    }

    public static String getType() {return type;}

    public static void setType(String type) {User.type = type;}
}
