package com.CurrencyApp.CurrencyConvertor.Service;

import com.CurrencyApp.CurrencyConvertor.Controller.CurrencyExchangeController;
import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import com.CurrencyApp.CurrencyConvertor.Model.Currency;

import com.CurrencyApp.CurrencyConvertor.Model.Response;
import com.CurrencyApp.CurrencyConvertor.Repository.CurrencyExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class CurrencyExchangeService{
      @Autowired
      CurrencyFetchService currencyFetchService;
      @Autowired
      CurrencyConversionService currencyConversionService;
      @Autowired
      CurrencyExchangeRepository currencyExchangeRepository;

        private static final Logger logger = LogManager.getLogger(CurrencyExchangeService.class);

    public CurrencyExchangeService(CurrencyExchangeRepository currencyExchangeRepository, CurrencyFetchService currencyFetchService, CurrencyConversionService currencyConversionService){
//        super();
        this.currencyExchangeRepository = currencyExchangeRepository;
        this.currencyFetchService = currencyFetchService;
        this.currencyConversionService = currencyConversionService;
    }

    public String dumpExchange(CurrencyExchange currencyExchange){
        this.currencyExchangeRepository.save(currencyExchange);
        logger.info("Request handled successfully.");
        return "Request Successful";
    }

    public List<CurrencyExchange> FetchExchange(LocalDate fromDate, LocalDate toDate) {
        List<CurrencyExchange> result = new ArrayList<>();
        LocalDate currentDate = fromDate;

        // Loop through the date range and fetch data for each date
        while (!currentDate.isAfter(toDate)) {
            try {
                // Fetch and store exchange data for the current date
                CurrencyExchange exchangeData = getCurrencyExchange(currentDate);
                result.add(exchangeData);
                logger.info("Fetched data for date: {}", currentDate);
            } catch (Exception e) {
                // Log the error and handle it if needed
                logger.error("Failed to fetch data for date: {}. Error: {}", currentDate, e.getMessage(), e);
            }
            currentDate = currentDate.plusDays(1); // Move to the next date
        }

        logger.info("Fetched data from public API and dumped into DB for date range: {} to {}", fromDate, toDate);
        logger.info("Request handled successfully.");
        return result;
    }

    public String DumpExchange(LocalDate toDate, LocalDate fromDate){
        this.currencyFetchService.FetchExchange(toDate, fromDate);
        logger.info("Fetched data from public API and dumped into DB");
        logger.info("Request handled successfully.");
        return "Request Successful";
    }

    public Response ConversionRate(Currency currency){
        return this.currencyConversionService.Conversion(getCurrencyExchange(currency.getDate()), currency);
    }

    public CurrencyExchange getCurrencyExchange(LocalDate date) {
        try {
            CurrencyExchange exchangeData = currencyExchangeRepository.findByDate(date);
            if (exchangeData == null) {
                logger.info("Data not found for {}. Fetching from public API and saving to DB.", date);
                currencyFetchService.FetchExchange(date);
                exchangeData = currencyExchangeRepository.findByDate(date);
            }
            logger.info("Request for {} handled successfully.", date);
            return exchangeData;
        } catch (Exception exception) {
            logger.error("Exception while finding data for {}: {}", date, exception.toString(), exception);
            throw exception;
        }
    }


    public List<CurrencyExchange> getAllCurrencyExchanges() {
        try {
            List<CurrencyExchange> result = currencyExchangeRepository.findAll();
            logger.info("Fetched all currency exchange data successfully, total records: {}", result.size());
            return result;
        } catch (Exception exception) {
            logger.error("Exception occurred while retrieving all currency exchange data: {}", exception.toString(), exception);
            throw exception;
        }
    }
}
