import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CurrencyConversionController {
    @Autowired
    private CurrencyConversionService conversionService;

//    @GetMapping("/convert")
//    public ResponseEntity<BigDecimal> convertCurrency(
//            @RequestParam String sourceCurrency,
//            @RequestParam String targetCurrency,
//            @RequestParam BigDecimal amount) {
//        BigDecimal convertedAmount = conversionService.convertCurrency(sourceCurrency, targetCurrency, amount);
//        return ResponseEntity.ok(convertedAmount);
//    }

//    @GetMapping("/exchange-rates")
//    public ResponseEntity<List<ExchangeRate>> getExchangeRates(
//            @RequestParam String sourceCurrency,
//            @RequestParam String targetCurrency) {
//        List<ExchangeRate> exchangeRates = conversionService.getExchangeRatesBySourceAndTarget(sourceCurrency, targetCurrency);
//        return ResponseEntity.ok(exchangeRates);
//    }

    @GetMapping("/Dump/{date}")
    public ResponseEntity<List<ExchangeRate>> getExchangeRatesByDate(@PathVariable String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        List<ExchangeRate> exchangeRates = conversionService.getExchangeRatesByDate(parsedDate);
        return ResponseEntity.ok(exchangeRates);
    }

//    @GetMapping("/latest-exchange-rate")
//    public ResponseEntity<ExchangeRate> getLatestExchangeRate(
//            @RequestParam String sourceCurrency,
//            @RequestParam String targetCurrency) {
//        ExchangeRate latestExchangeRate = conversionService.getLatestExchangeRate(sourceCurrency, targetCurrency);
//        return ResponseEntity.ok(latestExchangeRate);
//    }

    // Other RESTful endpoints for historical rates, portfolio tracking, etc.
}
