package com.example.booksscraper.controller;

import com.example.booksscraper.model.Book;
import com.example.booksscraper.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
public class ScraperController {

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String index(Model model) {
        try {
            List<String> categories = bookService.getCategories();
            model.addAttribute("categories", categories);
        } catch (IOException e) {
            model.addAttribute("error", "Error loading categories: " + e.getMessage());
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
}