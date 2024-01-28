package com.CurrencyApp.CurrencyConvertor.Repository;

import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange, String> {
//    List<CurrencyExchange> findByCurrencyId(String Id);
    CurrencyExchange findByDate(LocalDate Date);
}
