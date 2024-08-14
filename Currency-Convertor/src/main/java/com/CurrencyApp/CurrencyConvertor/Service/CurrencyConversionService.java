////Scenario
//    Example:
//            USD - > INR - > EUR - > CAD     and      USD - > CAD 
//               83.0    0.011    1.48                     1.34
//            
//                    = 1.35

package com.CurrencyApp.CurrencyConvertor.Service;

import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import com.CurrencyApp.CurrencyConvertor.Model.Currency;

import com.CurrencyApp.CurrencyConvertor.Repository.CurrencyExchangeRepository;
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

    public String Conversion( CurrencyExchange currencyExchange, Currency currency){
        List<Node> currencyRatioList = new ArrayList<Node>();
        ArrayList<String> addedCurrencies = new ArrayList<String>();

        // Nodes Creation for each Currency with It's Value with respect to EUR
        for(String currencyName : currencyExchange.getCurrenciesList()){
            if(currencyName.equals("EUR")) continue;
            currencyRatioList.add(new Node("EUR",currencyName,currencyExchange.getCurrencyRatio(currencyName)));
            currencyRatioList.add(new Node(currencyName,"EUR",1/currencyExchange.getCurrencyRatio(currencyName)));
        }
        // Nodes Creation for each Currency with It's Value 
        for(String toCurrencyName : currencyExchange.getCurrenciesList()){
            for(String fromCurrencyName : currencyExchange.getCurrenciesList()){
                if(!toCurrencyName.equals(fromCurrencyName) && !toCurrencyName.equals("EUR") && !fromCurrencyName.equals("EUR") ){
                    currencyRatioList.add(new Node(fromCurrencyName,toCurrencyName,currencyExchange.getCurrencyRatio(toCurrencyName)/currencyExchange.getCurrencyRatio(fromCurrencyName)));
                    currencyRatioList.add(new Node(toCurrencyName,fromCurrencyName,currencyExchange.getCurrencyRatio(fromCurrencyName)/currencyExchange.getCurrencyRatio(toCurrencyName)));
                }
            }
        }

//        Graph Creation
        Map<String, Map<String, Double>> currencyGraph = new HashMap<>();
        for (Node node: currencyRatioList) {
            if (!currencyGraph.containsKey(node.fromCurrency)) currencyGraph.put(node.fromCurrency, new HashMap<>());
            currencyGraph.get(node.fromCurrency).put(node.toCurrency, node.ratio);
            if (!currencyGraph.containsKey(node.toCurrency)) currencyGraph.put(node.toCurrency, new HashMap<>());
            currencyGraph.get(node.toCurrency).put(node.fromCurrency, 1.0/node.ratio);
        }

        return getRatio(currency.getFromCurrency(), currency.getToCurrency(), currencyGraph);
    }

    public String getRatio(String start, String end, Map<String, Map<String, Double>> map) {
        HashMap<String, Boolean> visited = new HashMap<>();
        HashMap<String, Double> distance = new HashMap<>();
        HashMap<String, String> paths = new HashMap<>();
        for(String cur: CurrencyExchange.getCurrenciesList()){
            distance.put(cur, Double.MAX_VALUE);
            visited.put(cur, false);
            paths.put(cur, "");
        }
        distance.put(start, 1.0);

        Queue<CurrencyRatio> queue = new PriorityQueue<>();
        queue.add(new CurrencyRatio(start,1.0));
        while(!queue.isEmpty()){
            String cur = queue.poll().getCurrency();
            if (!visited.get(cur)){
                visited.put(cur,true);
                for (var dest : map.get(cur).entrySet()){
                    if(distance.get(dest.getKey()) > distance.get(cur)*dest.getValue()){
                        distance.put(dest.getKey(), distance.get(cur)*dest.getValue());
                        paths.put(dest.getKey(), paths.get(dest.getKey())+"->"+cur);
                    }
                    queue.add(new CurrencyRatio(dest.getKey(),distance.get(dest.getKey())));
                }
            }
        }

//        HashMap<String, Double> DikstrasList = DFS(start, visited, distance, map);

        return "existingPath: "+start+"->"+end+"\nexcistingRatio:"+map.get(start).get(end)+"\n\nproposedPath: "+paths.get(end)+"->"+end+"\nproposedRatio:"+distance.get(end);
        }

//    public HashMap<String, Double> DFS(String root, HashMap<String, Boolean> visited, HashMap<String, Double> distance, Map<String, Map<String, Double>> map){
//        if (visited.get(root)){
//            return distance;
//        }
//        visited.put(root, true);
//        for(Map.Entry<String, Double> entry : map.get(root).entrySet())   {
//
//        }
//    }



}
