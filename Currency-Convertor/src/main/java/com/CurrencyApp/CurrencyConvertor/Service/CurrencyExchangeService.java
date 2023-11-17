package com.CurrencyApp.CurrencyConvertor.Service;

import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import com.CurrencyApp.CurrencyConvertor.Repository.CurrencyExchangeRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CurrencyExchangeService {
    CurrencyExchangeRepository currencyExchangeRepository;

    public CurrencyExchangeService(CurrencyExchangeRepository currencyExchangeRepository){
        this.currencyExchangeRepository = currencyExchangeRepository;
    }
    public String dumpExchange(CurrencyExchange currencyExchange){
        currencyExchangeRepository.save(currencyExchange);
        return "Success";
    }
    public CurrencyExchange getCurrencyExchange(String currencyExchangeId){
        if(currencyExchangeRepository.findById(currencyExchangeId).isEmpty()) {
            try {
                throw new Exception("Not Found");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return currencyExchangeRepository.findById(currencyExchangeId).get();
    }

    public List<CurrencyExchange> getAllCurrencyExchanges(){
        return currencyExchangeRepository.findAll();
    }
}
