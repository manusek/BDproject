package com.example.projektbd;

class Nationality {
    private int nationId; // Klucz podstawowy
    private String nationName;
    private String prodPlace;

    // Konstruktory, gettery i settery


    public Nationality() {
    }

    public Nationality(int nationId, String nationName, String prodPlace) {
        this.nationId = nationId;
        this.nationName = nationName;
        this.prodPlace = prodPlace;
    }

    public String getNationName() {
        return nationName;
    }

    public void setNationName(String nationName) {
        this.nationName = nationName;
    }

    public String getProdPlace() {
        return prodPlace;
    }

    public void setProdPlace(String prodPlace) {
        this.prodPlace = prodPlace;
    }

    // Klucz podstawowy
    public int getNationId() {
        return nationId;
    }

    public void setNationId(int nationId) {
        this.nationId = nationId;
    }
}