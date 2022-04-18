package com.HomeGarage.garage.modules;

public class BalanceAppModule {
    private float moneyForGarage , appPercent , totalBalance;

    public BalanceAppModule() {
    }

    public BalanceAppModule(float moneyForGarage, float appPercent , float totalBalance) {
        this.moneyForGarage = moneyForGarage;
        this.appPercent = appPercent;
        this.totalBalance = totalBalance;
    }

    public float getMoneyForGarage() {
        return moneyForGarage;
    }

    public void setMoneyForGarage(float moneyForGarage) {
        this.moneyForGarage = moneyForGarage;
    }

    public float getAppPercent() {
        return appPercent;
    }

    public void setAppPercent(float appPercent) {
        this.appPercent = appPercent;
    }

    public float getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(float totalBalance) {
        this.totalBalance = totalBalance;
    }
}
