package com.HomeGarage.garage.home.models;

public class SearchModel {
    private String nameGarage;
    private String addressGarage;

    public String getNameGarage() {
        return nameGarage;
    }

    public void setNameGarage(String nameGarage) {
        this.nameGarage = nameGarage;
    }

    public String getAddressGarage() {
        return addressGarage;
    }

    public void setAddressGarage(String addressGarage) {
        this.addressGarage = addressGarage;
    }

    public SearchModel(String nameGarage, String addressGarage) {
        this.nameGarage = nameGarage;
        this.addressGarage = addressGarage;
    }
}
