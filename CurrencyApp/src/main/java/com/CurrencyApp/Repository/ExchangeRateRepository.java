import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    // Custom query methods for retrieving exchange rates

    // Find exchange rates by source currency and target currency
//    List<ExchangeRate> findBySourceCurrencyAndTargetCurrency(String sourceCurrency, String targetCurrency);

    // Find exchange rates by date
    List<ExchangeRate> findByDate(LocalDate date);

    // Find the latest exchange rate for a specific currency pair
//    Optional<ExchangeRate> findTop1BySourceCurrencyAndTargetCurrencyOrderByDateDesc(String sourceCurrency, String targetCurrency);
}
