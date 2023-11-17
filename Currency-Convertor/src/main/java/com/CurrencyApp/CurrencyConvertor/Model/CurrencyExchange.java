package com.CurrencyApp.CurrencyConvertor.Model;

public class CurrencyExchange {
    private String fromCurrencyId;
    private String toCurrencyId;
    private String rate;
    public CurrencyExchange() {
    }

    public CurrencyExchange(String fromCurrencyId, String toCurrencyId, String rate){
        this.fromCurrencyId = fromCurrencyId;
        this.toCurrencyId = toCurrencyId;
        this.rate = rate;
    }

    public String getFromCurrencyId(){return fromCurrencyId;}
    public void setFromCurrencyId(String fromCurrencyId){ this.fromCurrencyId = fromCurrencyId;}
    public String getToCurrencyId(){return toCurrencyId;}
    public void setToCurrencyId(String toCurrencyId){ this.toCurrencyId = toCurrencyId;}
    public String getRate(){return rate;}
    public void setRate(String rate){ this.rate = rate;}



}
