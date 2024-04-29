package com.example.projektbd;

 class Ammunition {
    private int ammoId; // Klucz podstawowy
    private String name;
    private String description;

    // Konstruktory, gettery i settery


     public Ammunition() {
     }

     public Ammunition(int ammoId, String name, String description) {
         this.ammoId = ammoId;
         this.name = name;
         this.description = description;
     }

     public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
     }

     public String getDescription() {
         return description;
     }

     public void setDescription(String description) {
         this.description = description;
     }

     // Klucz podstawowy
    public int getAmmoId() {
        return ammoId;
    }

    public void setAmmoId(int ammoId) {
        this.ammoId = ammoId;
    }
}