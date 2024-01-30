package com.CurrencyApp.CurrencyConvertor.Service;

import com.CurrencyApp.CurrencyConvertor.Model.Currency;
import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import com.CurrencyApp.CurrencyConvertor.Repository.CurrencyExchangeRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CurrencyConversionService {

    CurrencyExchangeRepository currencyExchangeRepository;

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

    public String Conversion( CurrencyExchange currencyExchange, Currency currency){
        List<Node> currencyWebGraph = new ArrayList<Node>();
        ArrayList<String> addedCurrencies = new ArrayList<String>();

        for(String currencyName : currencyExchange.getCurrenciesList()){
            currencyWebGraph.add(new Node("EUR",currencyName,currencyExchange.getCurrencyRatio(currencyName)));
        }
        Double conversionRate = getRatio(currency.getFromCurrency(), currency.getToCurrency(), currencyWebGraph);
        return "Converted"+conversionRate+"Existing"+currencyExchange.getCurrencyRatio("USD");
    }

    public Double getRatio(String start, String end, List<Node> data) {
        Map<String, Map<String, Double>> map = new HashMap<>();
        for (Node node: data) {
            if (!map.containsKey(node.fromCurrency)) map.put(node.fromCurrency, new HashMap<>());
            map.get(node.fromCurrency).put(node.toCurrency, node.ratio);
            if (!map.containsKey(node.toCurrency)) map.put(node.toCurrency, new HashMap<>());
            map.get(node.toCurrency).put(node.fromCurrency, 1.0/node.ratio);
        }
        Queue<String> q = new LinkedList<>();
        Queue<Double> val = new LinkedList<>();
        q.offer(start);
        val.offer(1.0);
        Set<String> visited = new HashSet<>();
        while(!q.isEmpty()) {
            String cur = q.poll();
            double num = val.poll();
            if (visited.contains(cur)) continue;
            visited.add(cur);
            if (map.containsKey(cur)) {
                Map<String, Double> next = map.get(cur);
                for (String key: next.keySet()) {
                    if (!visited.contains(key)) {
                        q.offer(key);
                        if (key.equals(end)) return num*next.get(key);
                        val.offer(num*next.get(key));
                    }
                }
            }
        }
        return -1.0;
    }



}
