package com.CurrencyApp.CurrencyConvertor.Controller;

import com.CurrencyApp.CurrencyConvertor.CurrencyConvertorApplication;
import com.CurrencyApp.CurrencyConvertor.Model.Currency;
import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import com.CurrencyApp.CurrencyConvertor.Response.ResponseHandler;
import com.CurrencyApp.CurrencyConvertor.Service.CurrencyExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/Exchange")
public class CurrencyExchangeController
{
    @Autowired
    CurrencyExchangeService currencyExchangeService;

    public CurrencyExchangeController(CurrencyExchangeService currencyExchangeService){
        this.currencyExchangeService = currencyExchangeService;
    }

    @GetMapping("/{Date}")
    public ResponseEntity<Object> getExchangeDetails(@PathVariable("Date") String date)
    {
        LocalDate currentDate = LocalDate.parse(date);
        return ResponseHandler.responseBuilder("The currency values are with respect to 1 EUR",
                HttpStatus.OK, currencyExchangeService.getCurrencyExchange(currentDate));
    }

    @GetMapping("/All")
    public List<CurrencyExchange> getAllCurrencyExchanges(){
        return currencyExchangeService.getAllCurrencyExchanges();
    }

    @GetMapping("/LoadCurrrency/{toDate}/{fromDate}")
    public String loadCurrencyExchanges(
            @PathVariable("toDate") String toDate,
            @PathVariable("fromDate")String fromDate){
            LocalDate StartDate = LocalDate.parse(toDate);
            LocalDate EndDate = LocalDate.parse(fromDate);
            return currencyExchangeService.FetchExchange(StartDate, EndDate);
    }

    @GetMapping("/Conversion")
    public String convertCurrency(@RequestBody Currency currency){
        return currencyExchangeService.ConversionRate(currency);
    }
}