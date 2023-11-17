package com.CurrencyApp.CurrencyConvertor.Model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "currency_exchange_rates")
@ApiModel(description = "This table holds cloud vendor information.")
public class CurrencyExchange {
    @Id
    @ApiModelProperty(notes="This is a Cloud Vendor Id. It shall be unique.")
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
