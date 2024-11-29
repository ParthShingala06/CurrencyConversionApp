package com.CurrencyApp.CurrencyConvertor.Controller;

import com.CurrencyApp.CurrencyConvertor.Model.Currency;
import com.CurrencyApp.CurrencyConvertor.Model.Response;
import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import com.CurrencyApp.CurrencyConvertor.Response.ResponseHandler;
import com.CurrencyApp.CurrencyConvertor.Service.CurrencyExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CurrencyExchangeControllerTest {

    @Mock
    private CurrencyExchangeService currencyExchangeService;

    @InjectMocks
    private CurrencyExchangeController currencyExchangeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetExchangeDetails_ValidDate() {
        LocalDate date = LocalDate.of(2023, 10, 20);
        CurrencyExchange exchangeDetails = new CurrencyExchange.CurrencyExchangeBuilder()
                .setDate(date)
                .setAUD(0.0)
                .setBHD(0.0)
                .setCAD(0.0)
                .setCHF(0.0)
                .setCNY(0.0)
                .setGBD(0.0)
                .setINR(0.0)
                .setJPY(0.0)
                .setKYD(0.0)
                .setMXN(0.0)
                .setNZD(0.0)
                .setOMR(0.0)
                .setRUB(0.0)
                .setSGD(0.0)
                .setUSD(0.0)
                .setAUD(0.0)
                .build();

        when(currencyExchangeService.getCurrencyExchange(date)).thenReturn(exchangeDetails);

        ResponseEntity<Object> response = currencyExchangeController.getExchangeDetails("20-10-2023");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseHandler.responseBuilder("The currency values are with respect to 1 EUR", HttpStatus.OK, exchangeDetails).getBody(), response.getBody());
    }

    @Test
    void testGetExchangeDetails_InvalidDate() {
        ResponseEntity<Object> response = currencyExchangeController.getExchangeDetails("invalid-date");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Invalid date format. Please use yyyy-MM-dd.", response.getBody());
    }

    @Test
    void testGetCurrencyExchanges_ValidDateRange() {
        LocalDate startDate = LocalDate.of(2023, 10, 20);
        LocalDate endDate = LocalDate.of(2023, 10, 25);
        List<CurrencyExchange> exchangeList = Collections.emptyList(); // Replace with actual list for specific tests

        when(currencyExchangeService.fetchExchange(startDate, endDate)).thenReturn(exchangeList);

        ResponseEntity<Object> response = currencyExchangeController.getCurrencyExchanges("20-10-2023", "25-10-2023");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseHandler.responseBuilder("The currency values are with respect to 1 EUR", HttpStatus.OK, exchangeList).getBody(), response.getBody());
    }

    @Test
    void testLoadCurrencyExchanges_InvalidDate() {
        ResponseEntity<Object> response = currencyExchangeController.loadCurrencyExchanges("invalid-date", "another-invalid-date");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Invalid date format. Please use dd-MM-yyyy.", response.getBody());
    }

    @Test
    void testConvertCurrency_SuccessfulConversion() {
        Currency currency = new Currency(LocalDate.of(2023, 10, 20), "USD", "INR");
        Response conversionResult = new Response("1->2", 0.0, "1->3->2", 0.0);

        when(currencyExchangeService.conversionRate(currency)).thenReturn(conversionResult);

        ResponseEntity<Object> response = currencyExchangeController.convertCurrency(currency);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(conversionResult.toJson(), response.getBody());
    }

    @Test
    void testConvertCurrency_InvalidCurrency() {
        // Arrange
        Currency currency = new Currency(LocalDate.of(2023, 10, 20), "XYZ", "INR");
        Mockito.when(currencyExchangeService.conversionRate(currency)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = currencyExchangeController.convertCurrency(currency);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("Error occurred during currency conversion"));
    }




}
