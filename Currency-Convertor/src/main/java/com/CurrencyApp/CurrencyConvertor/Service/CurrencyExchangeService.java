package com.CurrencyApp.CurrencyConvertor.Service;

import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import com.CurrencyApp.CurrencyConvertor.Repository.CurrencyExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Service
public class CurrencyExchangeService {
      @Autowired
    CurrencyFetchService currencyFetchService;
    CurrencyExchangeRepository currencyExchangeRepository;

    public CurrencyExchangeService(CurrencyExchangeRepository currencyExchangeRepository, CurrencyFetchService currencyFetchService){
        this.currencyExchangeRepository = currencyExchangeRepository;
        this.currencyFetchService = currencyFetchService;
    }

    public String dumpExchange(CurrencyExchange currencyExchange){
        this.currencyExchangeRepository.save(currencyExchange);
        return "Success";
    }

    public String FetchExchange(LocalDate toDate, LocalDate fromDate){
        this.currencyFetchService.FetchExchange(toDate, fromDate);
        return "Success";
    }

    public CurrencyExchange getCurrencyExchange(LocalDate date){
        if(currencyExchangeRepository.findByDate(date) == null) {
            try {
                throw new Exception("Not Found");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return currencyExchangeRepository.findByDate(date);
    }

    public List<CurrencyExchange> getAllCurrencyExchanges(){
        return currencyExchangeRepository.findAll();
    }
}
