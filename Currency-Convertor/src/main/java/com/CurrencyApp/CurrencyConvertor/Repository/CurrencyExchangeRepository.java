package com.CurrencyApp.CurrencyConvertor.Repository;

import com.CurrencyApp.CurrencyConvertor.Model.CurrencyExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange, String> {
//    List<CurrencyExchange> findByCurrencyId(String Id);
    CurrencyExchange findByDate(LocalDate Date);
}
