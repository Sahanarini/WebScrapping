package com.poc.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

@Service
public class ScrapeService {

    private static final String URL = "https://www.amazon.in/";
    
    private final ChromeDriver driver;
    
    public ScrapeService(ChromeDriver driver) {
    	
        this.driver = driver;
    }

    public void scrape(final String value) {
        if (value == null || value.isEmpty()) {
            System.out.println("Search value cannot be null or empty.");
            return;
        }
        try {
            driver.get(URL);
            Thread.sleep(2000); // Wait for the page to load

            WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
            searchBox.sendKeys(value);
            searchBox.submit();
            Thread.sleep(2000); // Wait for the results to load

            List<WebElement> wordlist = driver.findElements(By.tagName("h2"));

            // Print results to console and write to Excel
            writeToExcel(wordlist, value); // Pass the search value here
            wordlist.forEach(word -> System.out.println(word.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Uncomment if you want to close the driver after the scrape
            // driver.quit();
        }
    }

    private void writeToExcel(List<WebElement> wordlist, String searchValue) {
        // Sanitize the sheet name to ensure it's valid
        String sanitizedSheetName = sanitizeSheetName(searchValue);
        String downloadsPath = System.getProperty("user.home") + "/Downloads/SearchResults_" + sanitizedSheetName + ".xlsx";
        Workbook workbook = null;
        Sheet sheet;

        File excelFile = new File(downloadsPath);

        try {
            if (excelFile.exists()) {
                // Open the existing workbook
                FileInputStream inputStream = new FileInputStream(excelFile);
                workbook = new XSSFWorkbook(inputStream);
                // Get the existing sheet or create a new one if it doesn't exist
                sheet = workbook.getSheet(sanitizedSheetName);
                if (sheet == null) {
                    sheet = workbook.createSheet(sanitizedSheetName);
                }
            } else {
                // Create a new workbook and sheet
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet(sanitizedSheetName);
            }

            // Find the last row number to append new data
            int rowCount = sheet.getPhysicalNumberOfRows();

            for (WebElement word : wordlist) {
                Row row = sheet.createRow(rowCount++);
                String text = word.getText();
                row.createCell(0).setCellValue(text);
            }

            // Write to the file
            try (FileOutputStream outputStream = new FileOutputStream(downloadsPath)) {
                workbook.write(outputStream);
                System.out.println("Results saved to: " + downloadsPath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Ensure workbook is closed if it was opened
            try {
                if (workbook != null) {
                    workbook.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String sanitizeSheetName(String name) {
        // Replace invalid characters with underscores
        return name.replaceAll("[\\/:*?\"<>|]", "_");
    }
    
    public List<String> readFromExcel(String searchValue) {
        String sanitizedSheetName = sanitizeSheetName(searchValue);
        String downloadsPath = System.getProperty("user.home") + "/Downloads/SearchResults_" + sanitizedSheetName + ".xlsx";
        List<String> results = new ArrayList<>();
        
        try (FileInputStream inputStream = new FileInputStream(new File(downloadsPath))) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(sanitizedSheetName);
            DataFormatter dataFormatter = new DataFormatter();
            
            if (sheet != null) {
                for (Row row : sheet) {
                    String cellValue = dataFormatter.formatCellValue(row.getCell(0));
                    results.add(cellValue);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return results;
    }
    
    
}
