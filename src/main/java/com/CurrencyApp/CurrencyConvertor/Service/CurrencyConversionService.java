////Scenario
//    Example:
//            USD - > INR - > EUR - > CAD     and      USD - > CAD 
//               83.0    0.011    1.48                     1.34
//            
//                    = 1.35

package com.CurrencyApp.CurrencyConvertor.Service;

import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import com.CurrencyApp.CurrencyConvertor.Model.Currency;
import com.CurrencyApp.CurrencyConvertor.Model.Response;

import com.CurrencyApp.CurrencyConvertor.Repository.CurrencyExchangeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CurrencyConversionService {
    CurrencyExchangeRepository currencyExchangeRepository;

    private static final Logger logger = LogManager.getLogger(CurrencyExchangeService.class);

    static class Node{
        String fromCurrency;
        String toCurrency;
        Double ratio;

        public Node(String fromCurrency, String toCurrency, Double ratio){
            this.fromCurrency = fromCurrency;
            this.toCurrency = toCurrency;
            this.ratio = ratio;
        }
    }
    public static class CurrencyRatio implements Comparable<CurrencyRatio>{
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
        @Override
        public int compareTo(CurrencyRatio o) {
            if (this.ratio == o.ratio) {
                return 1;
            } else {
                return Double.compare(this.ratio, o.ratio);
            }
        }
    }

    private void traverseGraph(List<Node> graph) {
        System.out.println("Traversing the currency graph:");
        for (Node node : graph) {
            System.out.println("From: " + node.fromCurrency + ", To: " + node.toCurrency + ", Ratio: " + node.ratio);
        }
    }

    public void printGraph(Map<String, Map<String, Double>> map) {
        for (String fromCurrency : map.keySet()) {
            System.out.println(fromCurrency + ":");
            Map<String, Double> edges = map.get(fromCurrency);
            for (String toCurrency : edges.keySet()) {
                System.out.println("  -> " + toCurrency + " (" + edges.get(toCurrency) + ")");
            }
        }
    }

    // Dikstra's Implementation
    public Response getRatio(String start, String end, Map<String, Map<String, Double>> map) {
        HashMap<String, Boolean> visited = new HashMap<>();
        HashMap<String, Double> distance = new HashMap<>();
        HashMap<String, String> paths = new HashMap<>();

        // Initialize entities
        for (String cur : CurrencyExchange.getCurrenciesList()) {
            distance.put(cur, Double.MAX_VALUE);
            visited.put(cur, false);
            paths.put(cur, "");
        }

        // Set the starting currency ratio as 1
        distance.put(start, 1.0);

        Queue<CurrencyRatio> queue = new PriorityQueue<>();
        queue.add(new CurrencyRatio(start, 1.0));

        while (!queue.isEmpty()) {
            String cur = queue.poll().getCurrency();

            if (!visited.get(cur)) {
                visited.put(cur, true);

                // Process neighbors
                for (var dest : map.getOrDefault(cur, Collections.emptyMap()).entrySet()) {
                    Double newDistance = distance.get(cur) * dest.getValue();

                    if (distance.get(dest.getKey()) > newDistance) {
                        distance.put(dest.getKey(), newDistance);
                        paths.put(dest.getKey(), paths.get(cur) + "->" + cur);
                    }

                    queue.add(new CurrencyRatio(dest.getKey(), distance.get(dest.getKey())));
                }
            }
        }

        // Handle cases where the path or distance may not be available
        String existingPath = paths.getOrDefault(end, "Path not found");
        Double existingRate = map.getOrDefault(start, Collections.emptyMap()).getOrDefault(end, null);
        String proposedPath = paths.getOrDefault(end, "Path not found") + "->" + end;
        Double proposedRate = distance.getOrDefault(end, null);

        if (existingRate == null) {
            logger.warn("No exchange rate found from {} to {}", start, end);
        }

        logger.info("Successfully completed the request. Start: {}, End: {}", start, end);
        logger.debug("Existing Path: {}, Existing Rate: {}", existingPath, existingRate);
        logger.debug("Proposed Path: {}, Proposed Rate: {}", proposedPath, proposedRate);

        return new Response(
                start + "->" + end,
                existingRate,
                proposedPath,
                proposedRate
        );
    }

    public Response Conversion(CurrencyExchange currencyExchange, Currency currency) {
        List<Node> currencyRatioList = new ArrayList<>();

        try {
            // Nodes Creation for each Currency with Its Value with respect to EUR
            for (String currencyName : CurrencyExchange.getCurrenciesList()) {
                if (currencyName.equals("EUR")) continue;

                double ratioToEUR = currencyExchange.getCurrencyRatio(currencyName);
                double ratioFromEUR = 1 / ratioToEUR;

                currencyRatioList.add(new Node("EUR", currencyName, ratioToEUR));
                currencyRatioList.add(new Node(currencyName, "EUR", ratioFromEUR));
            }

            // Nodes Creation for each Currency with Its Value
            for (String toCurrencyName : CurrencyExchange.getCurrenciesList()) {
                for (String fromCurrencyName : CurrencyExchange.getCurrenciesList()) {
                    if (!toCurrencyName.equals(fromCurrencyName) && !toCurrencyName.equals("EUR") && !fromCurrencyName.equals("EUR")) {
                        double ratioTo = currencyExchange.getCurrencyRatio(toCurrencyName) / currencyExchange.getCurrencyRatio(fromCurrencyName);
                        double ratioFrom = currencyExchange.getCurrencyRatio(fromCurrencyName) / currencyExchange.getCurrencyRatio(toCurrencyName);

                        currencyRatioList.add(new Node(fromCurrencyName, toCurrencyName, ratioTo));
                        currencyRatioList.add(new Node(toCurrencyName, fromCurrencyName, ratioFrom));
                    }
                }
            }

            // Graph Creation
            Map<String, Map<String, Double>> currencyGraph = new HashMap<>();
            for (Node node : currencyRatioList) {
                currencyGraph.computeIfAbsent(node.fromCurrency, k -> new HashMap<>())
                        .put(node.toCurrency, node.ratio);

                currencyGraph.computeIfAbsent(node.toCurrency, k -> new HashMap<>())
                        .put(node.fromCurrency, 1.0 / node.ratio);
            }

            // Conversion Call
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
