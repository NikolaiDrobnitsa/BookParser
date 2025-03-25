package com.example.booksscraper.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "price", nullable = false)
    private String price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "rating")
    private String rating;

    @Column(name = "availability")
    private String availability;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private BookCategory category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public BookEntity() {
    }

    public BookEntity(String title, String price, String imageUrl, String rating, String availability, BookCategory category) {
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.availability = availability;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }

    public static BookEntity fromDto(Book bookDto, BookCategory category) {
        return new BookEntity(
                bookDto.getTitle(),
                bookDto.getPrice(),
                bookDto.getImageUrl(),
                bookDto.getRating(),
                bookDto.getAvailability(),
                category
        );
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}