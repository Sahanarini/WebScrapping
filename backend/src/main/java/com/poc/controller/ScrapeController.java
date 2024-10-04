package com.poc.controller;

import com.poc.service.ScrapeService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scrape")
@CrossOrigin("*")
public class ScrapeController {

    private final ScrapeService scrapeService;

    @Autowired
    public ScrapeController(ScrapeService scrapeService) {
        this.scrapeService = scrapeService;
    }

    @PostMapping
    public String scrape(@RequestBody String searchValue) {
        // Trim any whitespace from the input
        searchValue = searchValue.trim();
        scrapeService.scrape(searchValue);
        return "Scraping initiated for: " + searchValue;
    }
    
    @GetMapping("/results")
    public ResponseEntity<List<String>> getResults(@RequestParam String searchValue) {
        List<String> results = scrapeService.readFromExcel(searchValue);
        return ResponseEntity.ok(results);
    }
}
