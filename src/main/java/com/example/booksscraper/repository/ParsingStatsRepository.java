package com.example.booksscraper.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.booksscraper.model.ParsingStats;

@Repository
public interface ParsingStatsRepository extends JpaRepository<ParsingStats, Integer> {
    List<ParsingStats> findAllByOrderByTimestampDesc();
}