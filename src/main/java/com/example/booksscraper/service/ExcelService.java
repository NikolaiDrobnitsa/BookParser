package com.example.booksscraper.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.example.booksscraper.model.BookEntity;
import com.example.booksscraper.model.CurrencyRate;
import com.example.booksscraper.model.ParsingStats;

@Service
public class ExcelService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ByteArrayInputStream generateBooksReport(List<BookEntity> books, String category) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(category + " Книги");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"Назва", "Ціна", "Рейтинг", "Наявність", "Дата додавання"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIdx = 1;
            for (BookEntity book : books) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(book.getTitle());
                row.createCell(1).setCellValue(book.getPrice());
                row.createCell(2).setCellValue(book.getRating());
                row.createCell(3).setCellValue(book.getAvailability());
                row.createCell(4).setCellValue(
                        book.getCreatedAt() != null ? book.getCreatedAt().format(DATE_FORMATTER) : ""
                );
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Помилка генерації Excel звіту: " + e.getMessage());
        }
    }

    public ByteArrayInputStream generateStatsReport(List<ParsingStats> statsList) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Статистика парсингу");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"Категорія", "Кількість книг", "Середня ціна", "Дата"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIdx = 1;
            for (ParsingStats stats : statsList) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(stats.getCategoryName());
                row.createCell(1).setCellValue(stats.getTotalBooks());
                row.createCell(2).setCellValue(stats.getAvgPrice() != null ? stats.getAvgPrice() : 0);
                row.createCell(3).setCellValue(
                        stats.getTimestamp() != null ? stats.getTimestamp().format(DATE_FORMATTER) : ""
                );
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Помилка генерації Excel звіту: " + e.getMessage());
        }
    }

    public ByteArrayInputStream generateCurrencyReport(List<CurrencyRate> rates) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Курси валют");

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"Валюта", "Курс купівлі", "Курс продажу", "Дата"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIdx = 1;
            for (CurrencyRate rate : rates) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(rate.getCurrencyName());
                row.createCell(1).setCellValue(rate.getBuyRate());
                row.createCell(2).setCellValue(rate.getSellRate());
                row.createCell(3).setCellValue(
                        rate.getFetchDate() != null ? rate.getFetchDate().format(DATE_FORMATTER) : ""
                );
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Помилка генерації Excel звіту: " + e.getMessage());
        }
    }
}