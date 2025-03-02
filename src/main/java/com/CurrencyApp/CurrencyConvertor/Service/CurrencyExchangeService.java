package com.CurrencyApp.CurrencyConvertor.Service;

import com.CurrencyApp.CurrencyConvertor.Controller.CurrencyExchangeController;
import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import com.CurrencyApp.CurrencyConvertor.Model.Currency;
import com.CurrencyApp.CurrencyConvertor.Model.Response;
import com.CurrencyApp.CurrencyConvertor.Repository.CurrencyExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class CurrencyExchangeService {

    @Autowired
    private CurrencyFetchService currencyFetchService;

    @Autowired
    private CurrencyConversionService currencyConversionService;

    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;

    private static final Logger logger = LogManager.getLogger(CurrencyExchangeService.class);


    /**
     * Fetches currency exchange data between two dates, calls an API for each date in the range, and stores the data.
     *
     * @param fromDate The start date of the data fetch
     * @param toDate The end date of the data fetch
     * @return A list of CurrencyExchange objects fetched for the given date range
     */
    public List<CurrencyExchange> fetchExchange(LocalDate fromDate, LocalDate toDate) {
        List<CurrencyExchange> result = new ArrayList<>();
        LocalDate currentDate = fromDate;

        // Loop through the date range and fetch data for each day
        while (!currentDate.isAfter(toDate)) {
            try {
                CurrencyExchange exchangeData = getCurrencyExchange(currentDate);
                result.add(exchangeData);

                logger.info("Fetched and stored data for date: {}", currentDate);
            } catch (Exception e) {
                logger.error("Failed to fetch data for date: {}. Error: {}", currentDate, e.getMessage(), e);
            }
            currentDate = currentDate.plusDays(1); // Move to the next day
        }

        logger.info("Fetched and stored data from {} to {}", fromDate, toDate);
        return result;
    }

    /**
     * Fetches currency exchange data between two dates asynchronously, calls an API for each date in the range, and stores the data.
     *
     * @param fromDate The start date of the data fetch
     * @param toDate The end date of the data fetch
     * @return A CompletableFuture wrapping a list of CurrencyExchange objects fetched for the given date range
     */
    @Async
    public CompletableFuture<List<CurrencyExchange>> fetchExchangeAsync(LocalDate fromDate, LocalDate toDate) {
        List<CurrencyExchange> result = new ArrayList<>();
        LocalDate currentDate = fromDate;

        while (!currentDate.isAfter(toDate)) {
            try {
                CurrencyExchange exchangeData = getCurrencyExchange(currentDate);
                result.add(exchangeData);
                logger.info("Fetched and stored data for date: {}", currentDate);
            } catch (Exception e) {
                logger.error("Failed to fetch data for date: {}. Error: {}", currentDate, e.getMessage(), e);
            }
            currentDate = currentDate.plusDays(1); // Move to the next day
        }

        logger.info("Fetched and stored data from {} to {}", fromDate, toDate);
        return CompletableFuture.completedFuture(result);
    }

    /**
     * Triggers the fetch of currency exchange data from a public API for the given date range and stores it in the database asynchronously.
     *
     * @param toDate The end date of the fetch
     * @param fromDate The start date of the fetch
     * @return A CompletableFuture wrapping a success message
     */
    @Async
    public CompletableFuture<String> dumpExchangeAsync(LocalDate toDate, LocalDate fromDate) {
        currencyFetchService.fetchExchange(toDate, fromDate);
        logger.info("Fetched data from public API and stored in database.");
        return CompletableFuture.completedFuture("Request Successful");
    }

    /**
     * Triggers the fetch of currency exchange data from a public API for the given date range and stores it in the database.
     *
     * @param toDate The end date of the fetch
     * @param fromDate The start date of the fetch
     * @return A success message indicating the operation was successful
     */
    public String dumpExchange(LocalDate toDate, LocalDate fromDate) {
        this.currencyFetchService.fetchExchange(toDate, fromDate);
        logger.info("Fetched data from public API and stored in database.");
        return "Request Successful";
    }

    /**
     * Converts the given currency using the exchange rate of the specified date asynchronously.
     *
     * @param currency The currency object containing conversion details
     * @return A CompletableFuture wrapping a Response object with the conversion result
     */
    @Async
    public CompletableFuture<Response> conversionRateAsync(Currency currency) {
        return CompletableFuture.supplyAsync(() -> currencyConversionService.conversion(getCurrencyExchange(currency.getDate()), currency));
    }

    /**
     * Converts the given currency using the exchange rate of the specified date.
     *
     * @param currency The currency object containing conversion details
     * @return A Response object with the conversion result
     */
    public Response conversionRate(Currency currency) {
        return this.currencyConversionService.conversion(getCurrencyExchange(currency.getDate()), currency);
    }

    /**
     * Retrieves the currency exchange data for a specific date asynchronously. If no data is found in the database,
     * it fetches the data from a public API and saves it.
     *
     * @param date The date for which to retrieve the exchange data
     * @return A CompletableFuture wrapping the CurrencyExchange object for the given date
     */
    @Async
    public CompletableFuture<CurrencyExchange> getCurrencyExchangeAsync(LocalDate date) {
        return CompletableFuture.supplyAsync(() -> getCurrencyExchange(date));
    }
    /**
     * Retrieves the currency exchange data for a specific date. If no data is found in the database,
     * it fetches the data from a public API and saves it.
     *
     * @param date The date for which to retrieve the exchange data
     * @return The CurrencyExchange object for the given date
     */
    public CurrencyExchange getCurrencyExchange(LocalDate date) {
        try {
            // Try to fetch the data from the database
            CurrencyExchange exchangeData = currencyExchangeRepository.findByDate(date);

            // If no data is found, fetch from public API
            if (exchangeData == null) {
                logger.info("Data not found for {}. Fetching from public API.", date);
                currencyFetchService.fetchExchange(date);
                exchangeData = currencyExchangeRepository.findByDate(date);
            }

            logger.info("Successfully handled request for {}", date);
            return exchangeData;
        } catch (Exception exception) {
            logger.error("Error while retrieving data for {}: {}", date, exception.getMessage(), exception);
            throw exception;
        }
    }

    /**
     * Retrieves all currency exchange data from the database asynchronously.
     *
     * @return A CompletableFuture wrapping a list of all CurrencyExchange records
     */
    @Async
    public CompletableFuture<List<CurrencyExchange>> getAllCurrencyExchangesAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<CurrencyExchange> result = currencyExchangeRepository.findAll();
                logger.info("Successfully fetched all currency exchange data. Total records: {}", result.size());
                return result;
            } catch (Exception exception) {
                logger.error("Error while retrieving all currency exchange data: {}", exception.getMessage(), exception);
                throw exception;
            }
        });
    }

    /**
     * Retrieves all currency exchange data from the database.
     *
     * @return A list of all CurrencyExchange records
     */
    public List<CurrencyExchange> getAllCurrencyExchanges() {
        try {
            List<CurrencyExchange> result = currencyExchangeRepository.findAll();
            logger.info("Successfully fetched all currency exchange data. Total records: {}", result.size());
            return result;
        } catch (Exception exception) {
            logger.error("Error while retrieving all currency exchange data: {}", exception.getMessage(), exception);
            throw exception;
        }
    }
}
