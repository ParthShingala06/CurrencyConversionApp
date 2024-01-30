package com.CurrencyApp.CurrencyConvertor.Service;

import com.CurrencyApp.CurrencyConvertor.Model.Currency;
import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import com.CurrencyApp.CurrencyConvertor.Repository.CurrencyExchangeRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class CurrencyConversionService {

    CurrencyExchangeRepository currencyExchangeRepository;


    public String Conversion(CurrencyExchange currencyExchange, Currency currency){
        return currencyExchange.toString() + currency.toString();
    }


}
