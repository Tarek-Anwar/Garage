package com.HomeGarage.garage.home.models;

public class FindModels {
    private int imgFind;
    private String txtFind;

    public FindModels(int imgFind, String txtFind) {
        this.imgFind = imgFind;
        this.txtFind = txtFind;
    }

    public int getImgFind() {
        return imgFind;
    }

    public void setImgFind(int imgFind) {
        this.imgFind = imgFind;
    }

    public String getTxtFind() {
        return txtFind;
    }

    public void setTxtFind(String txtFind) {
        this.txtFind = txtFind;
    }
}
