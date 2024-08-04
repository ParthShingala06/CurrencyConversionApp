package com.CurrencyApp.CurrencyConvertor.Service;

import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import com.CurrencyApp.CurrencyConvertor.Model.Currency;

import com.CurrencyApp.CurrencyConvertor.Repository.CurrencyExchangeRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class CurrencyExchangeService{
      @Autowired
      CurrencyFetchService currencyFetchService;
      @Autowired
      CurrencyConversionService currencyConversionService;
      @Autowired
      CurrencyExchangeRepository currencyExchangeRepository;

    public CurrencyExchangeService(CurrencyExchangeRepository currencyExchangeRepository, CurrencyFetchService currencyFetchService, CurrencyConversionService currencyConversionService){
//        super();
        this.currencyExchangeRepository = currencyExchangeRepository;
        this.currencyFetchService = currencyFetchService;
        this.currencyConversionService = currencyConversionService;
    }

    public String dumpExchange(CurrencyExchange currencyExchange){
        this.currencyExchangeRepository.save(currencyExchange);
        return "Success";
    }

    public String FetchExchange(LocalDate toDate, LocalDate fromDate){
        this.currencyFetchService.FetchExchange(toDate, fromDate);
        return "Success";
    }

    public String ConversionRate(Currency currency){
        return this.currencyConversionService.Conversion(getCurrencyExchange(currency.getDate()), currency);
    }

    public CurrencyExchange getCurrencyExchange(LocalDate date){
        if(currencyExchangeRepository.findByDate(date) == null) {
            currencyFetchService.FetchExchange(date);
        }
        return currencyExchangeRepository.findByDate(date);
    }

    public List<CurrencyExchange> getAllCurrencyExchanges(){
        return currencyExchangeRepository.findAll();
    }
}
