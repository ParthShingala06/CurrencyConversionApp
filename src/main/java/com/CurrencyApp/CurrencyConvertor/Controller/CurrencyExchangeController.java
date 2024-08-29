package com.CurrencyApp.CurrencyConvertor.Controller;

import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import com.CurrencyApp.CurrencyConvertor.Model.Currency;
import com.CurrencyApp.CurrencyConvertor.Response.ResponseHandler;
import com.CurrencyApp.CurrencyConvertor.Service.CurrencyExchangeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/Exchange")
public class CurrencyExchangeController
{
    private static final Logger logger = LogManager.getLogger(CurrencyExchangeController.class);

    @Autowired
    CurrencyExchangeService currencyExchangeService;

    public CurrencyExchangeController(CurrencyExchangeService currencyExchangeService){
        this.currencyExchangeService = currencyExchangeService;
    }

    @GetMapping("/get/{Date}")
    @Operation(summary = "Get exchange details for a specific date",
            description = "Fetches currency exchange details for a given date.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of exchange details"),
                    @ApiResponse(responseCode = "400", description = "Invalid date format"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Object> getExchangeDetails(@PathVariable("Date") String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate currentDate;

        // Log the incoming request
        logger.info("\n========== Request to get exchange details for date: {} ==========", date);

        try {
            // Parse the date using the defined format
            currentDate = LocalDate.parse(date, formatter);

            // Fetch the exchange details
            Object exchangeDetails = currencyExchangeService.getCurrencyExchange(currentDate);

            // Log success and return the result
            logger.info("Successfully fetched exchange details for date: {}", currentDate);
            return ResponseHandler.responseBuilder("The currency values are with respect to 1 EUR",
                    HttpStatus.OK, exchangeDetails);

        } catch (DateTimeParseException e) {
            // Log the date parsing error
            logger.error("Date parsing error: {}", e.getMessage(), e);
            return ResponseHandler.responseBuilder("Invalid date format. Please use yyyy-MM-dd.",
                    HttpStatus.BAD_REQUEST, e.getMessage());

        } catch (Exception e) {
            // Log any other errors
            logger.error("Error fetching exchange details: {}", e.getMessage(), e);
            return ResponseHandler.responseBuilder("Error fetching exchange details",
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping("/getAll/{toDate}/{fromDate}")
    @Operation(summary = "Get currency exchanges for a date range",
            description = "Fetches currency exchange details for a given date range.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of currency exchanges"),
                    @ApiResponse(responseCode = "400", description = "Invalid date format"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Object> getCurrencyExchanges(
            @PathVariable("toDate") String toDate,
            @PathVariable("fromDate") String fromDate) {
        // Define the date format expected
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate startDate;
        LocalDate endDate;
        logger.info("\n========== Request to get currency exchanges for given dates ==========");
        try {
            // Parse the dates using the defined format
            startDate = LocalDate.parse(toDate, formatter);
            endDate = LocalDate.parse(fromDate, formatter);
        } catch (DateTimeParseException e) {
            // Log and handle invalid date format
            logger.error("Date parsing error: {}", e.getMessage(), e);
            return ResponseHandler.responseBuilder("Invalid date format. Please use dd-MM-yyyy.",
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
        logger.info("Fetching currency exchange data from {} to {}", startDate, endDate);
        return ResponseHandler.responseBuilder("The currency values are with respect to 1 EUR",
                HttpStatus.OK, currencyExchangeService.FetchExchange(startDate, endDate));
    }

    @GetMapping("/loadCurrencyToDB/{toDate}/{fromDate}")
    @Operation(summary = "Load currency exchanges into the database for a date range",
            description = "Fetches and saves currency exchange data into the database for a given date range.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Currency exchanges successfully loaded into the database"),
                    @ApiResponse(responseCode = "400", description = "Invalid date format"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Object> loadCurrencyExchanges(
            @PathVariable("toDate") String toDate,
            @PathVariable("fromDate") String fromDate) {
        // Define the date format expected
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate startDate;
        LocalDate endDate;
        logger.info("\n========== Request to load currency exchanges for given dates ==========");
        try {
            // Parse the dates using the defined format
            startDate = LocalDate.parse(toDate, formatter);
            endDate = LocalDate.parse(fromDate, formatter);
        } catch (DateTimeParseException e) {
            // Log and handle invalid date format
            logger.error("Date parsing error: {}", e.getMessage(), e);
            return ResponseHandler.responseBuilder("Invalid date format. Please use dd-MM-yyyy.",
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
        logger.info("Fetching currency exchange data from {} to {}", startDate, endDate);
        return ResponseHandler.responseBuilder("Request Successfull",
                HttpStatus.OK, currencyExchangeService.DumpExchange(startDate, endDate));
    }

    @PostMapping(value="/smartConversion", produces = "application/json")
    @Operation(summary = "Perform smart currency conversion",
            description = "Converts currency using the best available rates.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Currency object containing conversion details",
                    required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful currency conversion"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Object> convertCurrency(@RequestBody Currency currency) {
        logger.info("\n========== Request Received for Currency Conversion: From {} to {} on {} ==========",
                currency.getFromCurrency(), currency.getToCurrency(), currency.getDate());

        try {
            // Perform the currency conversion
            String conversionResult = currencyExchangeService.ConversionRate(currency).toJson();

            // Log success and return the result
            logger.info("Currency conversion successful: From {} to {} on {}",
                    currency.getFromCurrency(), currency.getToCurrency(), currency.getDate());
            return ResponseEntity.ok().body(conversionResult);

        } catch (Exception e) {
            // Log the error and return an error response
            logger.error("Error occurred during currency conversion: {}", e.getMessage(), e);
            return ResponseHandler.responseBuilder("Error during currency conversion. " +
                            "The Currency should be one of these: " + CurrencyExchange.getCurrenciesListString(),
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}