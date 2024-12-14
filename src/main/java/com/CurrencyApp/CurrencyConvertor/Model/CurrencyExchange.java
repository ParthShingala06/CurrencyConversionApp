package com.CurrencyApp.CurrencyConvertor.Model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Arrays;

@Entity
@Table(name = "currency_exchange_rates")
@ApiModel(description = "This table holds Exchange information by date.")
public class CurrencyExchange {
    @Id
    @ApiModelProperty(notes="")
    final private LocalDate date;
    final private Double USD;
    final private Double INR;
    final private Double RUB;
    final private Double BHD;
    final private Double OMR;
    final private Double GBD;
    final private Double CHF;
    final private Double NZD;
    final private Double AUD;
    final private Double SGD;
    final private Double CAD;
    final private Double KYD;
    final private Double CNY;
    final private Double JPY;
    final private Double MXN;
    final private Integer TotalCurrencies;

    private static String[] CurrenciesList;

    public CurrencyExchange() {
        // Default constructor for JPA
        this.date = null;
        this.USD = null;
        this.INR = null;
        this.RUB = null;
        this.BHD = null;
        this.OMR = null;
        this.GBD = null;
        this.CHF = null;
        this.NZD = null;
        this.AUD = null;
        this.SGD = null;
        this.CAD = null;
        this.KYD = null;
        this.CNY = null;
        this.JPY = null;
        this.MXN = null;
        this.TotalCurrencies = null;
    }

    // Using Builder Design Pattern
    public CurrencyExchange(CurrencyExchangeBuilder builder){
        this.date = builder.date;
        this.USD = builder.USD;
        this.INR = builder.INR;
        this.RUB = builder.RUB;
        this.BHD = builder.BHD;
        this.OMR = builder.OMR;
        this.GBD = builder.GBD;
        this.CHF = builder.CHF;
        this.NZD = builder.NZD;
        this.AUD = builder.AUD;
        this.SGD = builder.SGD;
        this.CAD = builder.CAD;
        this.KYD = builder.KYD;
        this.CNY = builder.CNY;
        this.JPY = builder.JPY;
        this.MXN = builder.MXN;

        TotalCurrencies = 15;
        CurrenciesList =  new String[]{"USD","INR","RUB","BHD","OMR","GBD","CHF","NZD","AUD","SGD","CAD","KYD","CNY","JPY","MXN","EUR"};
    }

    // Method to check if a given currency is valid
    public static boolean isValidCurrency(String currency) {
        if (currency == null || currency.isEmpty()) {
            return false;
        }
        return Arrays.asList(CurrenciesList).contains(currency.toUpperCase());
    }

    @Override
    public String toString() {
        return super.toString();
    }

    // Builder Class
    public static class CurrencyExchangeBuilder{
        private LocalDate date;
        private Double USD;
        private Double INR;
        private Double RUB;
        private Double BHD;
        private Double OMR;
        private Double GBD;
        private Double CHF;
        private Double NZD;
        private Double AUD;
        private Double SGD;
        private Double CAD;
        private Double KYD;
        private Double CNY;
        private Double JPY;
        private Double MXN;

        public CurrencyExchangeBuilder setDate(LocalDate date) {
            this.date = date;
            return this;
        }
        public CurrencyExchangeBuilder setUSD(Double USD) {
            this.USD = USD;
            return this;
        }
        public CurrencyExchangeBuilder setINR(Double INR) {
            this.INR = INR;
            return this;
        }
        public CurrencyExchangeBuilder setRUB(Double RUB) {
            this.RUB = RUB;
            return this;
        }
        public CurrencyExchangeBuilder setBHD(Double BHD) {
            this.BHD = BHD;
            return this;
        }
        public CurrencyExchangeBuilder setOMR(Double OMR) {
            this.OMR = OMR;
            return this;
        }
        public CurrencyExchangeBuilder setGBD(Double GBD) {
            this.GBD = GBD;
            return this;
        }
        public CurrencyExchangeBuilder setCHF(Double CHF) {
            this.CHF = CHF;
            return this;
        }
        public CurrencyExchangeBuilder setNZD(Double NZD) {
            this.NZD = NZD;
            return this;
        }
        public CurrencyExchangeBuilder setAUD(Double AUD) {
            this.AUD = AUD;
            return this;
        }
        public CurrencyExchangeBuilder setSGD(Double SGD) {
            this.SGD = SGD;
            return this;
        }
        public CurrencyExchangeBuilder setCAD(Double CAD) {
            this.CAD = CAD;
            return this;
        }
        public CurrencyExchangeBuilder setKYD(Double KYD) {
            this.KYD = KYD;
            return this;
        }
        public CurrencyExchangeBuilder setCNY(Double CNY) {
            this.CNY = CNY;
            return this;
        }
        public CurrencyExchangeBuilder setJPY(Double JPY) {
            this.JPY = JPY;
            return this;
        }
        public CurrencyExchangeBuilder setMXN(Double MXN) {
            this.MXN = MXN;
            return this;
        }
        public CurrencyExchange build() {
            return new CurrencyExchange(this);
        }
    }

    // Getters
    public LocalDate getDate() {
        return date;
    }
    public Double getUSD() { return USD;}
    public Double getINR() { return INR;}
    public Double getRUB() { return RUB;}
    public Double getBHD() { return BHD;}
    public Double getOMR() { return OMR;}
    public Double getGBD() { return GBD;}
    public Double getCHF() { return CHF;}
    public Double getNZD() { return NZD;}
    public Double getAUD() { return AUD;}
    public Double getSGD() { return SGD;}
    public Double getCAD() { return CAD;}
    public Double getKYD() { return KYD;}
    public Double getCNY() { return CNY;}
    public Double getJPY() { return JPY;}
    public Double getMXN() { return MXN;}
    public Integer getTotalCurrencies() { return TotalCurrencies; }
    public static String[] getCurrenciesList() { return CurrenciesList; }

    public static String getCurrenciesListString() { return String.join(", ", CurrenciesList); }

    public Double getCurrencyRatio(String currency){
        if(currency.equalsIgnoreCase("USD")){ return USD;}
        else if(currency.equalsIgnoreCase("INR")){ return INR;}
        else if(currency.equalsIgnoreCase("RUB")){ return RUB;}
        else if(currency.equalsIgnoreCase("BHD")){ return BHD;}
        else if(currency.equalsIgnoreCase("OMR")){ return OMR;}
        else if(currency.equalsIgnoreCase("GBD")){ return GBD;}
        else if(currency.equalsIgnoreCase("CHF")){ return CHF;}
        else if(currency.equalsIgnoreCase("NZD")){ return NZD;}
        else if(currency.equalsIgnoreCase("AUD")){ return AUD;}
        else if(currency.equalsIgnoreCase("SGD")){ return SGD;}
        else if(currency.equalsIgnoreCase("CAD")){ return CAD;}
        else if(currency.equalsIgnoreCase("KYD")){ return KYD;}
        else if(currency.equalsIgnoreCase("CNY")){ return CNY;}
        else if(currency.equalsIgnoreCase("JPY")){ return JPY;}
        else if(currency.equalsIgnoreCase("MXN")){ return MXN;}
        return null;
    }

}
