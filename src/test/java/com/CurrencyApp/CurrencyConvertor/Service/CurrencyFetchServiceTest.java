package com.CurrencyApp.CurrencyConvertor.Service;

import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import com.CurrencyApp.CurrencyConvertor.Repository.CurrencyExchangeRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class CurrencyFetchServiceTest {

    @Autowired
    private CurrencyFetchService currencyFetchService;

    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;

    @Test
    void testFetchExchangeByDateFromAPI() {
        // Arrange
        LocalDate testDate = LocalDate.of(2024, 12, 1);

        // Act
        currencyFetchService.fetchExchangeByDateFromAPI(testDate);

        // Assert
        CurrencyExchange savedData = currencyExchangeRepository.findByDate(testDate);

        Assertions.assertNotNull(savedData, "Exchange data should be saved in the database.");
        Assertions.assertEquals(testDate, savedData.getDate(), "The saved data date should match the test date.");
        Assertions.assertTrue(savedData.getUSD() > 0, "USD exchange rate should be greater than 0.");
    }

    @Test
    void testFetchExchangeForRange() {
        // Arrange
        LocalDate startDate = LocalDate.of(2024, 11, 29);
        LocalDate endDate = LocalDate.of(2024, 12, 1);

        // Act
        currencyFetchService.fetchExchange(startDate, endDate);

        // Assert
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            CurrencyExchange savedData = currencyExchangeRepository.findByDate(date);
            Assertions.assertNotNull(savedData, "Exchange data for date " + date + " should be saved in the database.");
            Assertions.assertEquals(date, savedData.getDate(), "The saved data date should match the test date.");
        }
    }
}
