package com.CurrencyApp.CurrencyConvertor.Service;

import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import com.CurrencyApp.CurrencyConvertor.Model.Currency;
import com.CurrencyApp.CurrencyConvertor.Model.Response;
import com.CurrencyApp.CurrencyConvertor.Repository.CurrencyExchangeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class CurrencyConversionService {
    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;

    private static final Logger logger = LogManager.getLogger(CurrencyExchangeService.class);

    /**
     * Converts currency based on the provided exchange data asynchronously.
     *
     * @param currencyExchange The currency exchange data.
     * @param currency         The currency object containing conversion details.
     * @return A CompletableFuture wrapping the Response object with the conversion result.
     */
    @Async
    public CompletableFuture<Response> conversionAsync(CurrencyExchange currencyExchange, Currency currency) {
        return CompletableFuture.supplyAsync(() -> conversion(currencyExchange, currency));
    }

    /**
     * Inner class representing a currency conversion node with source currency, destination currency, and conversion ratio.
     */
    static class Node {
        String fromCurrency;
        String toCurrency;
        Double ratio;

        public Node(String fromCurrency, String toCurrency, Double ratio) {
            this.fromCurrency = fromCurrency;
            this.toCurrency = toCurrency;
            this.ratio = ratio;
        }
    }

    /**
     * Inner class representing a currency with its conversion ratio, used for priority queue comparison.
     */
    public static class CurrencyRatio implements Comparable<CurrencyRatio> {
        String currency;
        Double ratio;

        public CurrencyRatio(String currency, Double ratio) {
            this.currency = currency;
            this.ratio = ratio;
        }

        public String getCurrency() {
            return currency;
        }

        public Double getRatio() {
            return ratio;
        }

        // Override compareTo() to compare currency ratio
        @Override
        public int compareTo(CurrencyRatio o) {
            return Double.compare(this.ratio, o.ratio);
        }
    }

    /**
     * Debugging method to print the entire graph for currency conversion.
     * @param map The graph containing currency conversion rates.
     */
    public void printGraph(Map<String, Map<String, Double>> map) {
        for (String fromCurrency : map.keySet()) {
            System.out.println(fromCurrency + ":");
            Map<String, Double> edges = map.get(fromCurrency);
            for (String toCurrency : edges.keySet()) {
                System.out.println("  -> " + toCurrency + " (" + edges.get(toCurrency) + ")");
            }
        }
    }

    /**
     * Uses Dijkstra's algorithm to find the best conversion path between two currencies.
     * @param start The starting currency.
     * @param end The destination currency.
     * @param map A map representing the conversion graph.
     * @return A Response object with the conversion path and rate.
     */
    public Response getRatio(String start, String end, Map<String, Map<String, Double>> map) {
        if (map == null || !map.containsKey(start) || !map.containsKey(end)) {
            logger.error("The conversion map is null or missing required currencies: {} or {}", start, end);
            return new Response(
                    start + "->" + end,
                    null,
                    "Path not found",
                    null
            );
        }
        HashMap<String, Boolean> visited = new HashMap<>();
        HashMap<String, Double> distance = new HashMap<>();
        HashMap<String, String> paths = new HashMap<>();

        // Initialize distances and paths for each currency
        for (String cur : CurrencyExchange.getCurrenciesList()) {
            distance.put(cur, Double.MAX_VALUE);
            visited.put(cur, false);
            paths.put(cur, "");
        }

        // Set the starting currency ratio to 1 (as it converts to itself)
        distance.put(start, 1.0);

        // Priority queue to process currencies based on their conversion ratios
        Queue<CurrencyRatio> queue = new PriorityQueue<>();
        queue.add(new CurrencyRatio(start, 1.0));

        // Process the graph using Dijkstra's algorithm
        while (!queue.isEmpty()) {
            String cur = queue.poll().getCurrency();

            // Process the current currency if it hasn't been visited
            if (!visited.get(cur)) {
                visited.put(cur, true);

                // Explore neighbors and update their distances
                for (var dest : map.getOrDefault(cur, Collections.emptyMap()).entrySet()) {
                    Double newDistance = distance.get(cur) * dest.getValue();

                    // Update distance and path if a better path is found
                    if (distance.get(dest.getKey()) > newDistance) {
                        distance.put(dest.getKey(), newDistance);
                        paths.put(dest.getKey(), paths.get(cur) + "->" + cur);
                    }

                    // Add the updated currency to the queue for further processing
                    queue.add(new CurrencyRatio(dest.getKey(), distance.get(dest.getKey())));
                }
            }
        }

        // Retrieve the computed path and conversion rate
        String proposedPath = paths.getOrDefault(end, "Path not found") + "->" + end;
        Double proposedRate = distance.getOrDefault(end, null);

        logger.info("Successfully completed the request. Start: {}, End: {}", start, end);
        logger.debug("Proposed Path: {}, Proposed Rate: {}", proposedPath, proposedRate);

        // Return the conversion result
        return new Response(
                start + "->" + end,
                map.getOrDefault(start, Collections.emptyMap()).getOrDefault(end, null),
                proposedPath,
                proposedRate
        );
    }

    /**
     * Converts currency based on the provided exchange data.
     * @param currencyExchange The currency exchange data.
     * @param currency The currency object containing conversion details.
     * @return A Response object with the conversion result.
     */
    public Response conversion(CurrencyExchange currencyExchange, Currency currency) {
        List<Node> currencyRatioList = new ArrayList<>();
        if (!CurrencyExchange.isValidCurrency(currency.getFromCurrency()) ||
                !CurrencyExchange.isValidCurrency(currency.getToCurrency())) {
            throw new IllegalArgumentException("Not a valid Currency: " +
                    (CurrencyExchange.isValidCurrency(currency.getFromCurrency()) ? currency.getToCurrency() : currency.getFromCurrency()));
        }
        try {
            // Create nodes for conversion between currencies and EUR as a common base
            for (String currencyName : CurrencyExchange.getCurrenciesList()) {
                if (currencyName.equals("EUR")) continue;

                double ratioToEUR = currencyExchange.getCurrencyRatio(currencyName);
                double ratioFromEUR = 1 / ratioToEUR;

                // Add conversion ratios to/from EUR
                currencyRatioList.add(new Node("EUR", currencyName, ratioToEUR));
                currencyRatioList.add(new Node(currencyName, "EUR", ratioFromEUR));
            }

            // Create nodes for direct conversion between non-EUR currencies
            for (String toCurrencyName : CurrencyExchange.getCurrenciesList()) {
                for (String fromCurrencyName : CurrencyExchange.getCurrenciesList()) {
                    if (!toCurrencyName.equals(fromCurrencyName) && !toCurrencyName.equals("EUR") && !fromCurrencyName.equals("EUR")) {
                        double ratioTo = currencyExchange.getCurrencyRatio(toCurrencyName) / currencyExchange.getCurrencyRatio(fromCurrencyName);
                        double ratioFrom = currencyExchange.getCurrencyRatio(fromCurrencyName) / currencyExchange.getCurrencyRatio(toCurrencyName);

                        // Add conversion ratios between currencies
                        currencyRatioList.add(new Node(fromCurrencyName, toCurrencyName, ratioTo));
                        currencyRatioList.add(new Node(toCurrencyName, fromCurrencyName, ratioFrom));
                    }
                }
            }

            // Create a graph representing conversion rates between currencies
            Map<String, Map<String, Double>> currencyGraph = new HashMap<>();
            for (Node node : currencyRatioList) {
                currencyGraph.computeIfAbsent(node.fromCurrency, k -> new HashMap<>())
                        .put(node.toCurrency, node.ratio);

                currencyGraph.computeIfAbsent(node.toCurrency, k -> new HashMap<>())
                        .put(node.fromCurrency, 1.0 / node.ratio);
            }

            // Use Dijkstra's algorithm to find the best conversion rate
            Response response = getRatio(currency.getFromCurrency(), currency.getToCurrency(), currencyGraph);

            logger.info("Conversion request completed from {} to {}", currency.getFromCurrency(), currency.getToCurrency());
            return response;
        } catch (Exception e) {
            logger.error("Error during currency conversion from {} to {}: {}",
                    currency.getFromCurrency(), currency.getToCurrency(), e.getMessage(), e);
            throw e;
        }
    }
}
