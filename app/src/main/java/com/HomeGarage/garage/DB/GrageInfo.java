package com.HomeGarage.garage.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Entity")
public class GrageInfo  {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String grageName,governoate,city,restOfAddress,location ;
    private float price , viewRate;
    private int imgResourceId;

    @Ignore
    public GrageInfo(String grageName, String governoate, String city, String restOfAddress,
                     String location, float price, float viewRate, int imgResourceId) {
        this.grageName = grageName;
        this.governoate = governoate;
        this.city = city;
        this.restOfAddress = restOfAddress;
        this.location = location;
        this.price = price;
        this.viewRate = viewRate;
        this.imgResourceId = imgResourceId;
    }

    public GrageInfo(int id, String grageName, String governoate, String city, String restOfAddress,
                     String location, float price, float viewRate, int imgResourceId) {
        this.id = id;
        this.grageName = grageName;
        this.governoate = governoate;
        this.city = city;
        this.restOfAddress = restOfAddress;
        this.location = location;
        this.price = price;
        this.viewRate = viewRate;
        this.imgResourceId = imgResourceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGrageName() {
        return grageName;
    }

    public void setGrageName(String grageName) {
        this.grageName = grageName;
    }

    public String getGovernoate() {
        return governoate;
    }

    public void setGovernoate(String governoate) {
        this.governoate = governoate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRestOfAddress() {
        return restOfAddress;
    }

    public void setRestOfAddress(String restOfAddress) {
        this.restOfAddress = restOfAddress;
    }

    public int getImgResourceId() {
        return imgResourceId;
    }

    public void setImgResourceId(int imgResourceId) {
        this.imgResourceId = imgResourceId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getViewRate() {
        return viewRate;
    }

    public void setViewRate(float viewRate) {
        this.viewRate = viewRate;
    }
}
