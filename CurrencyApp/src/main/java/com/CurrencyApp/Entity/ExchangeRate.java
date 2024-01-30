import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    private String US;
    private String IND;
//    private BigDecimal rate;
    private LocalDate Date;

    public ExchangeRate() {
        // Default constructor
    }

    public ExchangeRate(String USCurrency, String INDCurrency, LocalDate date) {
        this.US = USCurrency;
        this.IND = INDCurrency;
//        this.rate = rate;
        this.date = date;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getSourceCurrency() {
        return USD;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.USD = sourceCurrency;
    }

    public String getTargetCurrency() {
        return IND;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.IND = targetCurrency;
    }

//    public BigDecimal getRate() {
//        return rate;
//    }

//    public void setRate(BigDecimal rate) {
//        this.rate = rate;
//    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "id=" + id +
                ", sourceCurrency='" + USD + '\'' +
                ", targetCurrency='" + IND + '\'' +
//                ", rate=" + rate +
                ", date=" + date +
                '}';
    }
}
