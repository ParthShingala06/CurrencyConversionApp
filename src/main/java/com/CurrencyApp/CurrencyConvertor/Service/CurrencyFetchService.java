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

    // Injecting the repository to interact with the database for storing exchange data
    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;

    // Logger for logging messages and errors
    private static final Logger logger = LogManager.getLogger(CurrencyFetchService.class);

    /**
     * Default constructor.
     */
    public CurrencyFetchService() {
    }

    /**
     * Fetches currency exchange data from a public API for a range of dates and stores it in the database.
     *
     * @param toDate   The start date of the range.
     * @param fromDate The end date of the range.
     * @return A success message indicating that the request was handled.
     */
    public String fetchExchange(LocalDate toDate, LocalDate fromDate) {
        LocalDate currentDate = toDate;

        while (!currentDate.isAfter(fromDate)) {
            try {
                fetchExchangeByDateFromAPI(currentDate);
            } catch (Exception e) {
                // Log errors if data fetch fails for a particular date
                logger.error("Failed to fetch exchange data for date {}: {}", currentDate, e.getMessage(), e);
            }
            currentDate = currentDate.plusDays(1);
        }
        return "Request Successful";
    }

    /**
     * Fetches currency exchange data for a specific date and stores it in the database.
     *
     * @param date The date for which to fetch the data.
     * @return A success message indicating that the request was handled.
     */
    public String fetchExchange(LocalDate date) {
        try {
            fetchExchangeByDateFromAPI(date);
        } catch (Exception e) {
            logger.error("Failed to fetch exchange data for date {}: {}", date, e.getMessage(), e);
        }
        return "Request Successful";
    }

    /**
     * Fetches currency exchange data from a public API for a given date and saves it.
     *
     * @param currentDate The date for which to fetch the data.
     */
    public void fetchExchangeByDateFromAPI(LocalDate currentDate) {
        // API base URL and endpoint structure
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
                // Read the API response if the connection is successful
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    exchangeDataDump(jsonResponse, currentDate);
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

    /**
     * Converts JSON response data into a CurrencyExchange object and saves it to the database.
     *
     * @param jsonResponse The JSON object containing currency exchange rates.
     * @param currentDate  The date for which the data is being stored.
     */
    public void exchangeDataDump(JSONObject jsonResponse, LocalDate currentDate) {
        try {
            CurrencyExchange currencyExchange = new CurrencyExchange.CurrencyExchangeBuilder()
                    .setDate(currentDate)
                    .setUSD(jsonResponse.getJSONObject("eur").getDouble("usd"))
                    .setINR(jsonResponse.getJSONObject("eur").getDouble("inr"))
                    .setRUB(jsonResponse.getJSONObject("eur").getDouble("rub"))
                    .setBHD(jsonResponse.getJSONObject("eur").getDouble("bhd"))
                    .setOMR(jsonResponse.getJSONObject("eur").getDouble("omr"))
                    .setGBD(jsonResponse.getJSONObject("eur").getDouble("gbp"))
                    .setCHF(jsonResponse.getJSONObject("eur").getDouble("chf"))
                    .setNZD(jsonResponse.getJSONObject("eur").getDouble("nzd"))
                    .setAUD(jsonResponse.getJSONObject("eur").getDouble("aud"))
                    .setSGD(jsonResponse.getJSONObject("eur").getDouble("sgd"))
                    .setCAD(jsonResponse.getJSONObject("eur").getDouble("cad"))
                    .setKYD(jsonResponse.getJSONObject("eur").getDouble("kyd"))
                    .setCNY(jsonResponse.getJSONObject("eur").getDouble("cny"))
                    .setJPY(jsonResponse.getJSONObject("eur").getDouble("jpy"))
                    .setMXN(jsonResponse.getJSONObject("eur").getDouble("mxn"))
                    .build();

            currencyExchangeRepository.save(currencyExchange);
            logger.info("Successfully saved exchange data for date: {}", currentDate);
        } catch (Exception e) {
            logger.error("Error while saving currency exchange data for date {}: {}", currentDate, e.getMessage(), e);
        }
    }
}
