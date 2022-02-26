package com.HomeGarage.garage.home.models;

import androidx.room.Ignore;


import java.util.Date;


public class Opreation {
    int id;
    String state,decisionMaker,decisionRecipient,address;
    double price;
    Date date;

    public Opreation(int id, String state, String decisionMaker, String decisionRecipient, String address,Date date,double price) {
        this.id = id;
        this.state = state;
        this.decisionMaker = decisionMaker;
        this.decisionRecipient = decisionRecipient;
        this.address = address;
        this.date = date;
        this.price=price;
    }
    public Opreation(String state, String decisionMaker, String decisionRecipient, String address, Date date,double price) {
        this.state = state;
        this.decisionMaker = decisionMaker;
        this.decisionRecipient = decisionRecipient;
        this.address = address;
        this.date = date;
        this.price=price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDecisionMaker() {
        return decisionMaker;
    }

    public void setDecisionMaker(String decisionMaker) {
        this.decisionMaker = decisionMaker;
    }


    public void setDecisionRecipient(String decisionRecipient) {
        this.decisionRecipient = decisionRecipient;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDecisionRecipient() {
        return decisionRecipient;
    }
}
