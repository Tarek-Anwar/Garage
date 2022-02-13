package com.HomeGarage.garage.home.models;

public class OffersModels {

    private int img;
    private String txtDetils;

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTxtDetils() {
        return txtDetils;
    }

    public void setTxtDetils(String txtDetils) {
        this.txtDetils = txtDetils;
    }

    public OffersModels(int img, String txtDetils) {
        this.img = img;
        this.txtDetils = txtDetils;
    }
}
