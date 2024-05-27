package com.example.projektbd;

public class listOfNations {

    int nationId;
    String nationName;
    String nationProd;

    public listOfNations(int nationId, String nationName, String nationProd) {
        this.nationId = nationId;
        this.nationName = nationName;
        this.nationProd = nationProd;
    }

    public int getNationId() {
        return nationId;
    }

    public void setNationId(int nationId) {
        this.nationId = nationId;
    }

    public String getNationName() {
        return nationName;
    }

    public void setNationName(String nationName) {
        this.nationName = nationName;
    }

    public String getNationProd() {
        return nationProd;
    }

    public void setNationProd(String nationProd) {
        this.nationProd = nationProd;
    }
}
