package com.HomeGarage.garage.modules;


import com.google.android.gms.maps.model.LatLng;

public class GarageInfoModule {

    private float Balance;
    private String id;
    private String nameEn, nameAr;
    private String phone;
    private String email;
    private String governoateEn, governoateAR;
    private String cityEn, cityAr;
    private String restOfAddressEN, restOfAddressAr;
    private String location;
    private float priceForHour;
    private String imageGarage;
    private float rate;
    private int numOfRatings;

    public GarageInfoModule() { }

    public int getNumOfRatings() {
        return numOfRatings;
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

    public float getBalance() {
        return Balance;
    }

    public void setBalance(float balance) {
        Balance = balance;
    }

    public String getId() {
        return id;
    }


    public String getNameEn() {
        return nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }

    public String getPhone() {
        return phone;
    }

    public String getGovernoateEn() {
        return governoateEn;
    }

    public String getGovernoateAR() {
        return governoateAR;
    }

    public String getCityEn() {
        return cityEn;
    }

    public String getCityAr() {
        return cityAr;
    }

    public String getRestOfAddressEN() {
        return restOfAddressEN;
    }

    public String getRestOfAddressAr() {
        return restOfAddressAr;
    }

    public String getLocation() {
        return location;
    }

    public float getPriceForHour() {
        return priceForHour;
    }

    public String getImageGarage() {
        return imageGarage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GarageInfoModule that = (GarageInfoModule) o;
        return id.equals(that.getId());

    }

}