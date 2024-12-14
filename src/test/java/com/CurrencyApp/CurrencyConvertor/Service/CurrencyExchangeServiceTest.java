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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CurrencyExchangeServiceTest {

    @Mock
    private CurrencyFetchService currencyFetchService;

    @Mock
    private CurrencyConversionService currencyConversionService;

    @Mock
    private CurrencyExchangeRepository currencyExchangeRepository;

    @InjectMocks
    private CurrencyExchangeService currencyExchangeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchExchange_Success() {
        // Arrange
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 3);

        CurrencyExchange mockExchange1 = new CurrencyExchange();
        CurrencyExchange mockExchange2 = new CurrencyExchange();
        CurrencyExchange mockExchange3 = new CurrencyExchange();

        when(currencyExchangeRepository.findByDate(any(LocalDate.class)))
                .thenReturn(mockExchange1, mockExchange2, mockExchange3);

        // Act
        List<CurrencyExchange> result = currencyExchangeService.fetchExchange(fromDate, toDate);

        // Assert
        assertEquals(3, result.size());
        verify(currencyExchangeRepository, times(3)).findByDate(any(LocalDate.class));
        verify(currencyFetchService, never()).fetchExchange(any(LocalDate.class));
    }


    @Test
    void testGetCurrencyExchange_FoundInDatabase() {
        // Arrange
        LocalDate testDate = LocalDate.of(2024, 12, 1);
        CurrencyExchange mockExchange = new CurrencyExchange();
        when(currencyExchangeRepository.findByDate(eq(testDate))).thenReturn(mockExchange);

        // Act
        CurrencyExchange result = currencyExchangeService.getCurrencyExchange(testDate);

        // Assert
        assertNotNull(result);
        assertEquals(mockExchange, result);
        verify(currencyExchangeRepository, times(1)).findByDate(eq(testDate));
        verify(currencyFetchService, never()).fetchExchange(any(LocalDate.class));
    }

    @Test
    void testGetCurrencyExchange_NotFoundInDatabase() {
        // Arrange
        LocalDate testDate = LocalDate.of(2024, 12, 1);
        when(currencyExchangeRepository.findByDate(eq(testDate))).thenReturn(null);

        // Act
        currencyExchangeService.getCurrencyExchange(testDate);

        // Assert
        verify(currencyExchangeRepository, times(2)).findByDate(eq(testDate)); // First check and after fetching
        verify(currencyFetchService, times(1)).fetchExchange(eq(testDate));
    }


    @Test
    void testGetAllCurrencyExchanges() {
        // Arrange
        CurrencyExchange mockExchange1 = new CurrencyExchange();
        CurrencyExchange mockExchange2 = new CurrencyExchange();

        when(currencyExchangeRepository.findAll()).thenReturn(Arrays.asList(mockExchange1, mockExchange2));

        // Act
        List<CurrencyExchange> result = currencyExchangeService.getAllCurrencyExchanges();

        // Assert
        assertEquals(2, result.size());
        verify(currencyExchangeRepository, times(1)).findAll();
    }
}
