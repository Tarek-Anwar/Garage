package com.HomeGarage.garage.home.models;

public class GarageViewModel {

    private String nameGarage;
    private String totalAddressGarage;
    private String locationGarage;
    private double rateGarage;
    private double priceForHour;

    public String getNameGarage() {
        return nameGarage;
    }

    public void setNameGarage(String nameGarage) {
        this.nameGarage = nameGarage;
    }

    public String getTotalAddressGarage() {
        return totalAddressGarage;
    }

    public void setTotalAddressGarage(String totalAddressGarage) {
        this.totalAddressGarage = totalAddressGarage;
    }

    public String getLocationGarage() {
        return locationGarage;
    }

    public void setLocationGarage(String locationGarage) {
        this.locationGarage = locationGarage;
    }

    public double getRateGarage() {
        return rateGarage;
    }

    public void setRateGarage(double rateGarage) {
        this.rateGarage = rateGarage;
    }

    public double getPriceForHour() {
        return priceForHour;
    }

    public void setPriceForHour(double priceForHour) {
        this.priceForHour = priceForHour;
    }

    public GarageViewModel(String nameGarage, String totalAddressGarage, String locationGarage, double rateGarage, double priceForHour) {
        this.nameGarage = nameGarage;
        this.totalAddressGarage = totalAddressGarage;
        this.locationGarage = locationGarage;
        this.rateGarage = rateGarage;
        this.priceForHour = priceForHour;
    }
}
