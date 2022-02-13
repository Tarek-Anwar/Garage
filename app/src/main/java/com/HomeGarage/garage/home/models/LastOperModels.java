package com.HomeGarage.garage.home.models;

public class LastOperModels {

    private String textTypeOper;
    private String textWhoDoOper;
    private String textWhoToDoOper;
    private String textTimeOper;
    private String textPlaceOper;

    public String getTextTypeOper() {
        return textTypeOper;
    }

    public void setTextTypeOper(String textTypeOper) {
        this.textTypeOper = textTypeOper;
    }

    public String getTextWhoDoOper() {
        return textWhoDoOper;
    }

    public void setTextWhoDoOper(String textWhoDoOper) {
        this.textWhoDoOper = textWhoDoOper;
    }

    public String getTextWhoToDoOper() {
        return textWhoToDoOper;
    }

    public void setTextWhoToDoOper(String textWhoToDoOper) {
        this.textWhoToDoOper = textWhoToDoOper;
    }

    public String getTextTimeOper() {
        return textTimeOper;
    }

    public void setTextTimeOper(String textTimeOper) {
        this.textTimeOper = textTimeOper;
    }

    public String getTextPlaceOper() {
        return textPlaceOper;
    }

    public void setTextPlaceOper(String textPlaceOper) {
        this.textPlaceOper = textPlaceOper;
    }

    public LastOperModels(String textTypeOper, String textWhoDoOper, String textWhoToDoOper, String textTimeOper, String textPlaceOper) {
        this.textTypeOper = textTypeOper;
        this.textWhoDoOper = textWhoDoOper;
        this.textWhoToDoOper = textWhoToDoOper;
        this.textTimeOper = textTimeOper;
        this.textPlaceOper = textPlaceOper;
    }
}
