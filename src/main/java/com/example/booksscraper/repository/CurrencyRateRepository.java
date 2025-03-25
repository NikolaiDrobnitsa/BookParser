package com.example.booksscraper.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booksscraper.model.CurrencyRate;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Integer> {
    Optional<CurrencyRate> findTopByCurrencyNameOrderByFetchDateDesc(String currencyName);
    List<CurrencyRate> findAllByOrderByFetchDateDesc();
}