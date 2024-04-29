package com.example.projektbd;

import java.util.Date;

class Tanks {
    private int tankId; // Klucz podstawowy
    private int nationId; // Klucz obcy
    private String name;
    private String type;
    private byte[] img;
    private String description;
    private int amount;
    private Date data;

    public Tanks() {
    }

    public Tanks(int tankId, int nationId, String name, String type, byte[] img, String description, int amount, Date data) {
        this.tankId = tankId;
        this.nationId = nationId;
        this.name = name;
        this.type = type;
        this.img = img;
        this.description = description;
        this.amount = amount;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    // Konstruktory, gettery i settery

    // Klucz podstawowy
    public int getTankId() {
        return tankId;
    }

    public void setTankId(int tankId) {
        this.tankId = tankId;
    }

    // Klucz obcy
    public int getNationId() {
        return nationId;
    }

    public void setNationId(int nationId) {
        this.nationId = nationId;
    }
}