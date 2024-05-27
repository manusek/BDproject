package com.example.projektbd;

public class listOfUsers {

    int userId;
    String userLogin;
    String userPass;
    String userAcc;
    String userEmail;

    public listOfUsers(int Id, String userLogin, String userEmail, String userPass, String userAcc ) {
        this.userId = Id;
        this.userLogin = userLogin;
        this.userPass = userPass;
        this.userAcc = userAcc;
        this.userEmail = userEmail;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserAcc() {
        return userAcc;
    }

    public void setUserAcc(String userAcc) {
        this.userAcc = userAcc;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
