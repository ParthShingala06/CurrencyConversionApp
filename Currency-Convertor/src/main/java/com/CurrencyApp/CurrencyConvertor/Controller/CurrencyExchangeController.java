package com.CurrencyApp.CurrencyConvertor.Controller;

import com.CurrencyApp.CurrencyConvertor.CurrencyConvertorApplication;
import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import com.CurrencyApp.CurrencyConvertor.Response.ResponseHandler;
import com.CurrencyApp.CurrencyConvertor.Service.CurrencyExchangeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Exchange")
public class CurrencyExchangeController {

    CurrencyExchangeService currencyExchangeService;

    public CurrencyExchangeController(CurrencyExchangeService currencyExchangeService){
        this.currencyExchangeService = currencyExchangeService;
    }

    @GetMapping("/{fromCurrencyId}")

    public ResponseEntity<Object> getExchangeDetails(@PathVariable("fromCurrencyId") String fromCurrencyId)
    {
        return ResponseHandler.responseBuilder("Requested Vendor Details are given here",
                HttpStatus.OK, currencyExchangeService.getCurrencyExchange(fromCurrencyId));
    }
    @GetMapping("/All")
    public List<CurrencyExchange> getAllCurrencyExchanges(){
        return currencyExchangeService.getAllCurrencyExchanges();
    }
}

