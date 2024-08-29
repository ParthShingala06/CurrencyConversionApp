package com.CurrencyApp.CurrencyConvertor.Service;

import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import com.CurrencyApp.CurrencyConvertor.Repository.CurrencyExchangeRepository;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.json.*;


import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class CurrencyFetchService {
    @Autowired
    CurrencyExchangeRepository currencyExchangeRepository;

    private static final Logger logger = LogManager.getLogger(CurrencyFetchService.class);

    public CurrencyFetchService() {
    }

    public String FetchExchange(LocalDate toDate, LocalDate fromDate) {
        LocalDate currentDate = toDate;
        while (!currentDate.isAfter(fromDate)) {
            try {
                FetchExchangeByDateFromAPI(currentDate);
            } catch (Exception e) {
                logger.error("Failed to fetch exchange data for date {}: {}", currentDate, e.getMessage(), e);
            }
            currentDate = currentDate.plusDays(1);
        }
        return "Request Successful";
    }

    public String FetchExchange(LocalDate date) {
        try {
            FetchExchangeByDateFromAPI(date);
        } catch (Exception e) {
            logger.error("Failed to fetch exchange data for date {}: {}", date, e.getMessage(), e);
        }
        return "Request Successful";
    }

    public void FetchExchangeByDateFromAPI(LocalDate currentDate) {
        String baseUrl = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@";
        String apiUrl = "/v1/currencies/eur.json";
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String urlString = baseUrl + formattedDate + apiUrl;

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    JSONObject jo = new JSONObject(response.toString());
                    ExchangeDataDump(jo, currentDate);
                }
            } else {
                logger.error("Failed to retrieve data for URL {}. HTTP Error Code: {}", urlString, responseCode);
            }
        } catch (IOException e) {
            logger.error("IO error while fetching data from URL {}: {}", urlString, e.getMessage(), e);
        } catch (JSONException e) {
            logger.error("JSON parsing error for URL {}: {}", urlString, e.getMessage(), e);
        }
    }

    public void ExchangeDataDump(JSONObject obj, LocalDate currentDate) {
        try {
            CurrencyExchange currencyObj = new CurrencyExchange(
                    currentDate,
                    obj.getJSONObject("eur").getDouble("usd"),
                    obj.getJSONObject("eur").getDouble("inr"),
                    obj.getJSONObject("eur").getDouble("rub"),
                    obj.getJSONObject("eur").getDouble("bhd"),
                    obj.getJSONObject("eur").getDouble("omr"),
                    obj.getJSONObject("eur").getDouble("gbp"),
                    obj.getJSONObject("eur").getDouble("chf"),
                    obj.getJSONObject("eur").getDouble("nzd"),
                    obj.getJSONObject("eur").getDouble("aud"),
                    obj.getJSONObject("eur").getDouble("sgd"),
                    obj.getJSONObject("eur").getDouble("cad"),
                    obj.getJSONObject("eur").getDouble("kyd"),
                    obj.getJSONObject("eur").getDouble("cny"),
                    obj.getJSONObject("eur").getDouble("jpy"),
                    obj.getJSONObject("eur").getDouble("mxn")
            );
            currencyExchangeRepository.save(currencyObj);
        } catch (Exception e) {
            logger.error("Error while saving currency exchange data for date {}: {}", currentDate, e.getMessage(), e);
        }
    }
}
