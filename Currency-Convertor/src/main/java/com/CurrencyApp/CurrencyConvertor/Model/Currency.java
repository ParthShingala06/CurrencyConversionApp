package com.CurrencyApp.CurrencyConvertor.Model;

public class Currency {
    private String currencyId;
    private String currency;

    public Currency(){
    }

    public Currency(String currencyId, String currency){
        this.currencyId = currencyId;
        this.currency = currency;
    }

    public String getCurrency(){return currency;}

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyId() {
        return currencyId;
    }
}
