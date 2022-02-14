package com.HomeGarage.garage.DB;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Entity")
public class GrageInfo {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String address,location ;
    private double price , viewRate;

    @Ignore
    public GrageInfo(String address, String location, double price, double viewRate) {
        this.address = address;
        this.location = location;
        this.price = price;
        this.viewRate = viewRate;
    }
    public GrageInfo(int id, String address, String location, double price, double viewRate) {
        this.id = id;
        this.address = address;
        this.location = location;
        this.price = price;
        this.viewRate = viewRate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getViewRate() {
        return viewRate;
    }

    public void setViewRate(double viewRate) {
        this.viewRate = viewRate;
    }
}
