package com.CurrencyApp.CurrencyConvertor.Service;

import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import com.CurrencyApp.CurrencyConvertor.Repository.CurrencyExchangeRepository;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.stereotype.Service;

import org.json.*;


import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class CurrencyFetchService {
    CurrencyExchangeRepository currencyExchangeRepository;

    public CurrencyFetchService(CurrencyExchangeRepository currencyExchangeRepository){
        this.currencyExchangeRepository = currencyExchangeRepository;
    }

    public String FetchExchange(LocalDate toDate, LocalDate fromDate){
        LocalDate CurrentDate = toDate;
        while(!CurrentDate.equals(fromDate)){
            CurrentDate = CurrentDate.plusDays(1);
            FetchExchangeByDateFromAPI(CurrentDate);
        }
        return "Success";
    }

    public void FetchExchangeByDateFromAPI(LocalDate CurrentDate){
        String data = "";
        try {
            String baseUrl = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/";
            String apiUrl = "currencies/eur.json";
            String formattedDate = CurrentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            URL url = new URL(baseUrl+"/"+formattedDate+"/"+apiUrl);

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
                    data = response.toString();
                    JSONObject jo = new JSONObject(data);

                    ExchangeDataDump(jo, CurrentDate);
                }
            } else {
                System.out.println("Failed to retrieve data. HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ExchangeDataDump(JSONObject obj, LocalDate CurrentDate){
        CurrencyExchange currencyObj = new CurrencyExchange(CurrentDate, obj.getJSONObject("eur").getDouble("usd"), obj.getJSONObject("eur").getDouble("inr"),
                obj.getJSONObject("eur").getDouble("rub"), obj.getJSONObject("eur").getDouble("bhd"),obj.getJSONObject("eur").getDouble("omr"),
                obj.getJSONObject("eur").getDouble("gbp"), obj.getJSONObject("eur").getDouble("chf"),obj.getJSONObject("eur").getDouble("nzd"),
                obj.getJSONObject("eur").getDouble("aud"), obj.getJSONObject("eur").getDouble("sgd"),obj.getJSONObject("eur").getDouble("cad"),
                obj.getJSONObject("eur").getDouble("kyd"), obj.getJSONObject("eur").getDouble("cny"),obj.getJSONObject("eur").getDouble("jpy"),
                obj.getJSONObject("eur").getDouble("mxn"));
        this.currencyExchangeRepository.save(currencyObj);
    }
}
