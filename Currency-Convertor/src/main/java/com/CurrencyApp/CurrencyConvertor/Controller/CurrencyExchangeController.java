package com.CurrencyApp.CurrencyConvertor.Controller;

import com.CurrencyApp.CurrencyConvertor.CurrencyConvertorApplication;
import com.CurrencyApp.CurrencyConvertor.Response.ResponseHandler;
import com.CurrencyApp.CurrencyConvertor.Service.CurrencyExchangeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Exchange")
public class CurrencyExchangeController {

    CurrencyExchangeService currencyExchangeService;
    CurrencyExchangeService cloudVendorService;

    public CurrencyExchangeController(CurrencyExchangeService currencyExchangeService){
        this.currencyExchangeService = currencyExchangeService;
    }

    public ResponseEntity<Object> getExchangeDetails(@PathVariable("currencyId") String currencyId)
    {
        return ResponseHandler.responseBuilder("Requested Vendor Details are given here",
                HttpStatus.OK, cloudVendorService.getCloudVendor(vendorId));
    }
}

