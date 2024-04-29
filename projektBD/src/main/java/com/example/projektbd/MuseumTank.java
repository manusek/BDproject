package com.example.projektbd;

class MuseumTank {
    private int museumId; // Klucz obcy
    private int tankId; // Klucz obcy

    // Konstruktory, gettery i settery


    public MuseumTank() {
    }

    public MuseumTank(int museumId, int tankId) {
        this.museumId = museumId;
        this.tankId = tankId;
    }



    // Klucz obcy museumId
    public int getMuseumId() {
        return museumId;
    }

    public void setMuseumId(int museumId) {
        this.museumId = museumId;
    }

    // Klucz obcy tankId
    public int getTankId() {
        return tankId;
    }

    public void setTankId(int tankId) {
        this.tankId = tankId;
    }
}