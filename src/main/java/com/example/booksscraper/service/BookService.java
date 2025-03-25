package com.example.booksscraper.service;

import com.example.booksscraper.model.Book;
import com.example.booksscraper.model.BookCategory;
import com.example.booksscraper.model.BookEntity;
import com.example.booksscraper.model.ParsingStats;
import com.example.booksscraper.repository.BookCategoryRepository;
import com.example.booksscraper.repository.BookRepository;
import com.example.booksscraper.repository.ParsingStatsRepository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private static final String BASE_URL = "https://books.toscrape.com/";

    @Autowired
    private BookCategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ParsingStatsRepository statsRepository;

    public List<String> getCategories() throws IOException {
        Document doc = Jsoup.connect(BASE_URL).get();
        Elements categoryElements = doc.select(".side_categories ul.nav-list li ul li a");

        List<String> categories = new ArrayList<>();
        for (Element categoryElement : categoryElements) {
            String categoryName = categoryElement.text();
            categories.add(categoryName);

            Optional<BookCategory> existingCategory = categoryRepository.findByName(categoryName);
            if (existingCategory.isEmpty()) {
                String relativeUrl = categoryElement.attr("href");
                String fullUrl = BASE_URL + relativeUrl;
                BookCategory category = new BookCategory(categoryName, fullUrl);
                categoryRepository.save(category);
            }
        }

        return categories;
    }

    @Transactional
    public List<Book> getBooksFromCategory(String category, int page) throws IOException {
        String categoryUrl = getCategoryUrl(category, page);
        Document doc = Jsoup.connect(categoryUrl).get();

        List<Book> books = new ArrayList<>();
        Elements bookElements = doc.select("article.product_pod");

        BookCategory categoryEntity = categoryRepository.findByName(category)
                .orElseGet(() -> {
                    BookCategory newCategory = new BookCategory(category, categoryUrl);
                    return categoryRepository.save(newCategory);
                });

        for (Element bookElement : bookElements) {
            Book book = new Book();

            book.setTitle(bookElement.select("h3 a").attr("title"));
            book.setPrice(bookElement.select(".price_color").text());

            String relativeImageUrl = bookElement.select("img").attr("src");
            book.setImageUrl(BASE_URL + relativeImageUrl.replace("../", ""));

            String ratingClass = bookElement.select(".star-rating").attr("class");
            book.setRating(ratingClass.replace("star-rating ", ""));

            book.setCategory(category);

            book.setAvailability(bookElement.select(".availability").text());

            books.add(book);

            BookEntity bookEntity = BookEntity.fromDto(book, categoryEntity);
            bookRepository.save(bookEntity);
        }

        updateParsingStats(category);

        return books;
    }

    private String getCategoryUrl(String category, int page) throws IOException {
        Optional<BookCategory> categoryEntity = categoryRepository.findByName(category);
        if (categoryEntity.isPresent()) {
            String categoryUrl = categoryEntity.get().getUrl();
            if (page == 1) {
                return categoryUrl;
            }
            return categoryUrl.replace("index.html", "page-" + page + ".html");
        }

        Document doc = Jsoup.connect(BASE_URL).get();
        Elements categoryElements = doc.select(".side_categories ul.nav-list li ul li a");

        for (Element categoryElement : categoryElements) {
            if (categoryElement.text().equalsIgnoreCase(category)) {
                String relativeCategoryUrl = categoryElement.attr("href");
                String categoryUrl = BASE_URL + relativeCategoryUrl;

                BookCategory newCategory = new BookCategory(category, categoryUrl);
                categoryRepository.save(newCategory);

                if (page == 1) {
                    return categoryUrl;
                }

                return categoryUrl.replace("index.html", "page-" + page + ".html");
            }
        }

        return BASE_URL;
    }

    public int getTotalPages(String category) throws IOException {
        String categoryUrl = getCategoryUrl(category, 1);
        Document doc = Jsoup.connect(categoryUrl).get();

        Element paginationElement = doc.selectFirst(".pager");

        if (paginationElement == null) {
            return 1;
        }

        String paginationText = paginationElement.selectFirst(".current").text().trim();

        return Integer.parseInt(paginationText.split(" of ")[1]);
    }

    private void updateParsingStats(String category) {
        long totalBooks = bookRepository.countByCategoryName(category);
        Double avgPrice = bookRepository.calculateAvgPriceByCategoryName(category);

        ParsingStats stats = new ParsingStats(category, (int) totalBooks, avgPrice);
        statsRepository.save(stats);
    }

    public List<ParsingStats> getAllStats() {
        return statsRepository.findAllByOrderByTimestampDesc();
    }

    public List<BookEntity> getBooksByCategoryFromDb(String category) {
        return bookRepository.findByCategoryName(category);
    }
}