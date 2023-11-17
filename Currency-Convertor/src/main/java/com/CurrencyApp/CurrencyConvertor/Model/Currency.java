package com.CurrencyApp.CurrencyConvertor.Model;

public class Currency {
    private Integer currencyId;
    private String currency;

    public Currency(){
    }

    public Currency(Integer currencyId, String currency){
        this.currencyId = currencyId;
        this.currency = currency;
    }

    public String getCurrency(){return currency;}

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }
}
