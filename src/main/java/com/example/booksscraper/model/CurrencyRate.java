package com.example.booksscraper.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "currency_rates")
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "currency_name", nullable = false)
    private String currencyName;

    @Column(name = "buy_rate", nullable = false)
    private Double buyRate;

    @Column(name = "sell_rate", nullable = false)
    private Double sellRate;

    @Column(name = "fetch_date")
    private LocalDateTime fetchDate;

    public CurrencyRate() {
    }

    public CurrencyRate(String currencyName, Double buyRate, Double sellRate) {
        this.currencyName = currencyName;
        this.buyRate = buyRate;
        this.sellRate = sellRate;
        this.fetchDate = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Double getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(Double buyRate) {
        this.buyRate = buyRate;
    }

    public Double getSellRate() {
        return sellRate;
    }

    public void setSellRate(Double sellRate) {
        this.sellRate = sellRate;
    }

    public LocalDateTime getFetchDate() {
        return fetchDate;
    }

    public void setFetchDate(LocalDateTime fetchDate) {
        this.fetchDate = fetchDate;
    }
}