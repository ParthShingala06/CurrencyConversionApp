package com.CurrencyApp.CurrencyConvertor.Model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "currency_exchange_rates")
@ApiModel(description = "This table holds Exchange information by date.")
public class CurrencyExchange {
    @Id
    @ApiModelProperty(notes="")
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
    private Integer TotalCurrencies = 15;
    private static String[] CurrenciesList =  {"USD","INR","RUB","BHD","OMR","GBD","CHF","NZD","AUD","SGD","CAD","KYD","CNY","JPY","MXN","EUR"};



    public CurrencyExchange(){

    }
    public CurrencyExchange(LocalDate date, Double USD, Double INR, Double RUB, Double BHD, Double OMR, Double GBD, Double CHF,
                            Double NZD, Double AUD, Double SGD, Double CAD, Double KYD, Double CNY, Double JPY, Double MXN){
        this.date = date;
        this.USD = USD;
        this.INR = INR;
        this.RUB = RUB;
        this.BHD = BHD;
        this.OMR = OMR;
        this.GBD = GBD;
        this.CHF = CHF;
        this.NZD = NZD;
        this.AUD = AUD;
        this.SGD = SGD;
        this.CAD = CAD;
        this.KYD = KYD;
        this.CNY = CNY;
        this.JPY = JPY;
        this.MXN = MXN;
    }
    public Double getCurrencyRatio(String currency){
        if(currency.toUpperCase().equals("USD")){ return USD;}
        else if(currency.toUpperCase().equals("INR")){ return INR;}
        else if(currency.toUpperCase().equals("RUB")){ return RUB;}
        else if(currency.toUpperCase().equals("BHD")){ return BHD;}
        else if(currency.toUpperCase().equals("OMR")){ return OMR;}
        else if(currency.toUpperCase().equals("GBD")){ return GBD;}
        else if(currency.toUpperCase().equals("CHF")){ return CHF;}
        else if(currency.toUpperCase().equals("NZD")){ return NZD;}
        else if(currency.toUpperCase().equals("AUD")){ return AUD;}
        else if(currency.toUpperCase().equals("SGD")){ return SGD;}
        else if(currency.toUpperCase().equals("CAD")){ return CAD;}
        else if(currency.toUpperCase().equals("KYD")){ return KYD;}
        else if(currency.toUpperCase().equals("CNY")){ return CNY;}
        else if(currency.toUpperCase().equals("JPY")){ return JPY;}
        else if(currency.toUpperCase().equals("MXN")){ return MXN;}
        return null;
    }
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



    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setUSD(Double USD) {this.USD = USD;}
    public void setINR(Double INR) {this.INR = INR;}
    public void setRUB(Double RUB) {this.RUB = RUB;}
    public void setBHD(Double BHD) {this.BHD = BHD;}
    public void setOMR(Double OMR) {this.OMR = OMR;}
    public void setGBD(Double GBD) {this.GBD = GBD;}
    public void setCHF(Double CHF) {this.CHF = CHF;}
    public void setNZD(Double NZD) {this.NZD = NZD;}
    public void setAUD(Double AUD) {this.AUD = AUD;}
    public void setSGD(Double SGD) {this.SGD = SGD;}
    public void setCAD(Double CAD) {this.CAD = CAD;}
    public void setKYD(Double KYD) {this.KYD = KYD;}
    public void setCNY(Double CNY) {this.CNY = CNY;}
    public void setJPY(Double JPY) {this.JPY = JPY;}
    public void setMXN(Double MXN) {this.MXN = MXN;}
    public void setTotalCurrencies(Integer totalCurrencies) {
        TotalCurrencies = totalCurrencies;
    }

    @Override
    public String toString() {
        return super.toString();
    }


}
