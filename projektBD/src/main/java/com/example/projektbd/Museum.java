package com.example.projektbd;


 class Museum {
    private int museumId; // Klucz podstawowy
    private String name;
    private String localization;

     public Museum() {
     }

     public Museum(int museumId, String name, String localization) {
         this.museumId = museumId;
         this.name = name;
         this.localization = localization;
     }

     public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
     }

     public String getLocalization() {
         return localization;
     }

     public void setLocalization(String localization) {
         this.localization = localization;
     }

     // Konstruktory, gettery i settery

    // Klucz podstawowy
    public int getMuseumId() {
        return museumId;
    }

    public void setMuseumId(int museumId) {
        this.museumId = museumId;
    }
}