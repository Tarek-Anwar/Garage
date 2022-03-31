package com.HomeGarage.garage.models;

public class GoverModel {
    private String governorate_name_ar, governorate_name_en;
    private String image_url;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public GoverModel() {
    }

    public GoverModel(String governorate_name_ar, String governorate_name_en) {
        this.governorate_name_ar = governorate_name_ar;
        this.governorate_name_en = governorate_name_en;

    }

    public String getGovernorate_name_ar() {
        return governorate_name_ar;
    }

    public void setGovernorate_name_ar(String governorate_name_ar) {
        this.governorate_name_ar = governorate_name_ar;
    }

    public String getGovernorate_name_en() {
        return governorate_name_en;
    }

    public void setGovernorate_name_en(String governorate_name_en) {
        this.governorate_name_en = governorate_name_en;
    }

}
