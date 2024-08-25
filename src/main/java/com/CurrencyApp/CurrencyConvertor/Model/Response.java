package com.CurrencyApp.CurrencyConvertor.Model;

public class Response {
    private final String existingPath;
    private final Double existingRate;
    private final String proposedPath;
    private final Double proposedRate;

    public Response(String existingPath, Double existingRate, String proposedPath, Double proposedRate){
        this.existingPath = existingPath;
        this.existingRate = existingRate;
        this.proposedPath = proposedPath;
        this.proposedRate = proposedRate;
    }

    // Method to create JSON string from the object
    public String toJson() {
        StringBuilder json = new StringBuilder("{");
        String message = (proposedRate > existingRate)
                ? "Found better rate"
                : "No better result then existing rate";

        json.append("\"result\":\"").append(message).append("\",");
        json.append("\"existing path\":\"").append(existingPath).append("\",");
        json.append("\"existing rate\":").append(existingRate).append(",");
        json.append("\"closest proposed path\":\"").append(proposedPath).append("\",");
        json.append("\"closest proposed rate\":").append(proposedRate).append(",");
        json.append("\"closest proposed rate\":").append(proposedRate).append(",");
        json.append("\"change percent\":").append( (proposedRate-existingRate)*100).append(",");
        double calculatedProfitPercent = (proposedRate > existingRate)
                ? (proposedRate / existingRate) * 100
                : 0.0;
        json.append("\"profit percent\":").append(calculatedProfitPercent);

        json.append("}");
        return json.toString();
    }
}
