package com.HomeGarage.garage.models;


import com.google.android.gms.maps.model.LatLng;

public class GarageInfoModel {

    private float Balance;
    private String id;
    private String nameEn, nameAr;
    private String phone;
    private String email;
    private String password;
    private String governoateEn, governoateAR;
    private String cityEn, cityAr;
    private String restOfAddressEN, restOfAddressAr;
    private String location;
    private float priceForHour;
    private String imageGarage;
    private float rate;
    private int numOfRatings;

    public int getNumOfRatings() {
        return numOfRatings;
    }

    public void setNumOfRatings(int numOfRatings) {
        this.numOfRatings = numOfRatings;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public LatLng getLatLngGarage() {
        String[] loc = getLocation().split(",");
        return new LatLng(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]));
    }

    public GarageInfoModel() {
    }

    public float getBalance() {
        return Balance;
    }

    public void setBalance(float balance) {
        Balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGovernoateEn() {
        return governoateEn;
    }

    public void setGovernoateEn(String governoateEn) {
        this.governoateEn = governoateEn;
    }

    public String getGovernoateAR() {
        return governoateAR;
    }

    public void setGovernoateAR(String governoateAR) {
        this.governoateAR = governoateAR;
    }

    public String getCityEn() {
        return cityEn;
    }

    public void setCityEn(String cityEn) {
        this.cityEn = cityEn;
    }

    public String getCityAr() {
        return cityAr;
    }

    public void setCityAr(String cityAr) {
        this.cityAr = cityAr;
    }

    public String getRestOfAddressEN() {
        return restOfAddressEN;
    }

    public void setRestOfAddressEN(String restOfAddressEN) {
        this.restOfAddressEN = restOfAddressEN;
    }

    public String getRestOfAddressAr() {
        return restOfAddressAr;
    }

    public void setRestOfAddressAr(String restOfAddressAr) {
        this.restOfAddressAr = restOfAddressAr;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getPriceForHour() {
        return priceForHour;
    }

    public void setPriceForHour(float priceForHour) {
        this.priceForHour = priceForHour;
    }

    public String getImageGarage() {
        return imageGarage;
    }

    public void setImageGarage(String imageGarage) {
        this.imageGarage = imageGarage;
    }
}