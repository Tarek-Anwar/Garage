package com.HomeGarage.garage.home.models;

public class CityModel {

    private  String city_name_ar , city_name_en , governorate_id ;
    private int numberGarage;

    public CityModel() { }

    public String getCity_name_ar() {
        return city_name_ar;
    }

    public void setCity_name_ar(String city_name_ar) {
        this.city_name_ar = city_name_ar;
    }

    public String getCity_name_en() {
        return city_name_en;
    }

    public void setCity_name_en(String city_name_en) {
        this.city_name_en = city_name_en;
    }

    public String getGovernorate_id() {
        return governorate_id;
    }

    public void setGovernorate_id(String governorate_id) {
        this.governorate_id = governorate_id;
    }

    public int getNumberGarage() {
        return numberGarage;
    }

    public void setNumberGarage(int numberGarage) {
        this.numberGarage = numberGarage;
    }
}
