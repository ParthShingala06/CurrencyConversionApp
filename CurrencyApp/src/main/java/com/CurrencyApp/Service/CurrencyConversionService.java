import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyConversionService {
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    public BigDecimal convertCurrency(String sourceCurrency, String targetCurrency, BigDecimal amount) {
        // Implement the conversion logic using exchange rates from the repository
        ExchangeRate exchangeRateOptional = exchangeRateRepository
                .findTop1BySourceCurrencyAndTargetCurrencyOrderByDateDesc(sourceCurrency, targetCurrency);

        if (exchangeRateOptional.isPresent()) {
            ExchangeRate exchangeRate = exchangeRateOptional.get();
            return amount.multiply(exchangeRate.getRate());
        }
//        else {
//            throw new ExchangeRateNotFoundException("Exchange rate not found for the specified currency pair.");
//        }
    }

    public List<ExchangeRate> getExchangeRatesBySourceAndTarget(String sourceCurrency, String targetCurrency) {
        return exchangeRateRepository.findBySourceCurrencyAndTargetCurrency(sourceCurrency, targetCurrency);
    }

    public List<ExchangeRate> getExchangeRatesByDate(LocalDate date) {
        return exchangeRateRepository.findByDate(date);
    }

    public ExchangeRate getLatestExchangeRate(String sourceCurrency, String targetCurrency) {
        return exchangeRateRepository
                .findTop1BySourceCurrencyAndTargetCurrencyOrderByDateDesc(sourceCurrency, targetCurrency);
//                .orElseThrow(() -> new ExchangeRateNotFoundException("Exchange rate not found for the specified currency pair."));
    }

    // Other methods for historical rates, portfolio tracking, etc.
}
