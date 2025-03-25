package com.example.booksscraper.controller;

import com.example.booksscraper.model.Book;
import com.example.booksscraper.model.BookEntity;
import com.example.booksscraper.model.CurrencyRate;
import com.example.booksscraper.model.ParsingStats;
import com.example.booksscraper.service.BookService;
import com.example.booksscraper.service.CurrencyService;
import com.example.booksscraper.service.ExcelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class ScraperController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ExcelService excelService;

    @GetMapping("/")
    public String index(Model model) {
        try {
            List<String> categories = bookService.getCategories();
            model.addAttribute("categories", categories);

            List<CurrencyRate> rates = currencyService.getLatestRates();
            if (rates.isEmpty()) {
                rates = currencyService.fetchCurrentRates();
            }
            model.addAttribute("currencyRates", rates);

            List<ParsingStats> stats = bookService.getAllStats();
            model.addAttribute("parsingStats", stats);

        } catch (IOException e) {
            model.addAttribute("error", "Помилка завантаження даних: " + e.getMessage());
        }

        return "index";
    }

    @GetMapping("/api/categories")
    @ResponseBody
    public List<String> getCategories() throws IOException {
        return bookService.getCategories();
    }

    @GetMapping("/api/books")
    @ResponseBody
    public List<Book> getBooks(
            @RequestParam String category,
            @RequestParam(defaultValue = "1") int page) throws IOException {

        return bookService.getBooksFromCategory(category, page);
    }

    @GetMapping("/api/pages")
    @ResponseBody
    public int getTotalPages(@RequestParam String category) throws IOException {
        return bookService.getTotalPages(category);
    }

    @GetMapping("/api/currency/refresh")
    @ResponseBody
    public List<CurrencyRate> refreshCurrencyRates() {
        return currencyService.fetchCurrentRates();
    }

    @GetMapping("/api/currency")
    @ResponseBody
    public List<CurrencyRate> getCurrencyRates() {
        return currencyService.getLatestRates();
    }

    @GetMapping("/export/books")
    public ResponseEntity<InputStreamResource> exportBooks(@RequestParam String category) {
        List<BookEntity> books = bookService.getBooksByCategoryFromDb(category);
        ByteArrayInputStream in = excelService.generateBooksReport(books, category);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=books_" + category + ".xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(in));
    }

    @GetMapping("/export/stats")
    public ResponseEntity<InputStreamResource> exportStats() {
        List<ParsingStats> stats = bookService.getAllStats();
        ByteArrayInputStream in = excelService.generateStatsReport(stats);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=parsing_stats.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(in));
    }

    @GetMapping("/export/currency")
    public ResponseEntity<InputStreamResource> exportCurrency() {
        List<CurrencyRate> rates = currencyService.getLatestRates();
        ByteArrayInputStream in = excelService.generateCurrencyReport(rates);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=currency_rates.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(in));
    }
}