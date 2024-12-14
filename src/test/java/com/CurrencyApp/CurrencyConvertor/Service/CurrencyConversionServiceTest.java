package com.CurrencyApp.CurrencyConvertor.Service;

import com.CurrencyApp.CurrencyConvertor.Model.Currency;
import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import com.CurrencyApp.CurrencyConvertor.Model.Response;
import com.CurrencyApp.CurrencyConvertor.Repository.CurrencyExchangeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyConversionServiceTest {

    @Mock
    private CurrencyExchangeRepository currencyExchangeRepository;

    @InjectMocks
    private CurrencyConversionService currencyConversionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRatio_Success() {
        // Arrange
        String start = "USD";
        String end = "INR";
        Map<String, Map<String, Double>> currencyGraph = new HashMap<>();

        currencyGraph.put("USD", Map.of("INR", 82.0, "EUR", 0.9));
        currencyGraph.put("INR", Map.of("USD", 1 / 82.0, "EUR", 0.011));
        currencyGraph.put("EUR", Map.of("USD", 1 / 0.9, "INR", 1 / 0.011));

        // Act
        Response response = currencyConversionService.getRatio(start, end, currencyGraph);

        // Assert
        assertNotNull(response);
    }

    @Test
    void testGetRatio_NoPathFound() {
        // Arrange
        String start = "USD";
        String end = "JPY";
        Map<String, Map<String, Double>> currencyGraph = new HashMap<>();
        currencyGraph.put("USD", Map.of("INR", 82.0));
        currencyGraph.put("INR", Map.of("USD", 1 / 82.0));

        // Act
        Response response = currencyConversionService.getRatio(start, end, currencyGraph);

        // Assert
        assertNotNull(response);
    }

    @Test
    void testConversion_Success() {
        // Arrange
        CurrencyExchange mockExchange = mock(CurrencyExchange.class);
        when(mockExchange.getCurrencyRatio("USD")).thenReturn(1.0);
        when(mockExchange.getCurrencyRatio("INR")).thenReturn(82.0);

        Currency currency = new Currency(LocalDate.now(), "USD", "INR");
        currency.setFromCurrency("USD");
        currency.setToCurrency("INR");

        // Act
        Response response = currencyConversionService.conversion(mockExchange, currency);

        // Assert
        assertNotNull(response);
    }

    @Test
    void testConversion_ErrorDuringGraphCreation() {
        // Arrange
        CurrencyExchange mockExchange = mock(CurrencyExchange.class);
        when(mockExchange.getCurrencyRatio(anyString())).thenThrow(new RuntimeException("Test Exception"));

        Currency currency = new Currency(LocalDate.now(), "USD", "INR");
        currency.setFromCurrency("USD");
        currency.setToCurrency("INR");

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            currencyConversionService.conversion(mockExchange, currency);
        });

        assertEquals("Test Exception", exception.getMessage());
    }
}
