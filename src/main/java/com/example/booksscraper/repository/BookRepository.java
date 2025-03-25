package com.example.booksscraper.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.booksscraper.model.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    List<BookEntity> findByCategoryName(String categoryName);

    @Query("SELECT COUNT(b) FROM BookEntity b WHERE b.category.name = ?1")
    long countByCategoryName(String categoryName);

    @Query("SELECT AVG(CAST(SUBSTRING(b.price, 2) AS double)) FROM BookEntity b WHERE b.category.name = ?1")
    Double calculateAvgPriceByCategoryName(String categoryName);
}