package com.CurrencyApp.CurrencyConvertor.Model;

import java.time.LocalDate;

public class Currency {
    private LocalDate date;
    private String toCurrency;
    private String fromCurrency;

    public Currency(){
    }

    public Currency(LocalDate date,String toCurrency, String fromCurrency){
        this.date = date;
        this.toCurrency = toCurrency;
        this.fromCurrency = fromCurrency;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
