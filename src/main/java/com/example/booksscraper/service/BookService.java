package com.example.booksscraper.service;

import com.example.booksscraper.model.Book;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private static final String BASE_URL = "https://books.toscrape.com/";

    public List<String> getCategories() throws IOException {
        Document doc = Jsoup.connect(BASE_URL).get();
        Elements categoryElements = doc.select(".side_categories ul.nav-list li ul li a");

        List<String> categories = new ArrayList<>();
        for (Element categoryElement : categoryElements) {
            categories.add(categoryElement.text());
        }

        return categories;
    }

    public List<Book> getBooksFromCategory(String category, int page) throws IOException {
        String categoryUrl = getCategoryUrl(category, page);
        Document doc = Jsoup.connect(categoryUrl).get();

        List<Book> books = new ArrayList<>();
        Elements bookElements = doc.select("article.product_pod");

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
        }

        return books;
    }

    private String getCategoryUrl(String category, int page) throws IOException {
        Document doc = Jsoup.connect(BASE_URL).get();
        Elements categoryElements = doc.select(".side_categories ul.nav-list li ul li a");

        for (Element categoryElement : categoryElements) {
            if (categoryElement.text().equalsIgnoreCase(category)) {
                String relativeCategoryUrl = categoryElement.attr("href");
                String categoryUrl = BASE_URL + relativeCategoryUrl;

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
}