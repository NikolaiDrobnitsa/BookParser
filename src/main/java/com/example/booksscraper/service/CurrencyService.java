package com.example.booksscraper.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.booksscraper.model.CurrencyRate;
import com.example.booksscraper.model.dto.PrivatBankCurrencyDTO;
import com.example.booksscraper.repository.CurrencyRateRepository;

@Service
public class CurrencyService {

    @Value("${privatbank.exchange.url}")
    private String privatBankUrl;

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<CurrencyRate> fetchCurrentRates() {
        try {
            PrivatBankCurrencyDTO[] response = restTemplate.getForObject(privatBankUrl, PrivatBankCurrencyDTO[].class);

            if (response != null) {
                List<CurrencyRate> rates = Arrays.stream(response)
                        .map(dto -> {
                            try {
                                double buyRate = Double.parseDouble(dto.getBuy());
                                double sellRate = Double.parseDouble(dto.getSale());
                                return new CurrencyRate(dto.getCurrency(), buyRate, sellRate);
                            } catch (NumberFormatException e) {
                                return null;
                            }
                        })
                        .filter(rate -> rate != null)
                        .collect(Collectors.toList());

                return currencyRateRepository.saveAll(rates);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currencyRateRepository.findAllByOrderByFetchDateDesc();
    }

    public List<CurrencyRate> getLatestRates() {
        return currencyRateRepository.findAllByOrderByFetchDateDesc();
    }
}