package com.HomeGarage.garage.DB;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "opEntity")
public class Opreation {

    @PrimaryKey(autoGenerate = true)
    int id;
    String state,decisionMaker,decisionRecipient,address;
    Date date;

    public Opreation(int id, String state, String decisionMaker, String decisionRecipient, String address, Date date) {
        this.id = id;
        this.state = state;
        this.decisionMaker = decisionMaker;
        this.decisionRecipient = decisionRecipient;
        this.address = address;
        this.date = date;
    }

    @Ignore
    public Opreation( String state, String decisionMaker, String decisionRecipient, String address, Date date) {
        this.state = state;
        this.decisionMaker = decisionMaker;
        this.decisionRecipient = decisionRecipient;
        this.address = address;
        this.date = date;
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

    public String getDecisionRecipient() {
        return decisionRecipient;
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

}
