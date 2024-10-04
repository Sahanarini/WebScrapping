package com.poc.config;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PostConstruct;

@Configuration
public class SeleniumConfiguration {

	@Value("${webdriver.chrome.driver}")
	private String chromeDriverPath;

	@PostConstruct
	void postConstruct() {
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
	}

	@Bean
	public ChromeDriver driver() {
		final ChromeOptions chromeop = new ChromeOptions();
		chromeop.addArguments("--headless");
		WebDriverManager.chromedriver().setup(); // This sets up the correct driver automatically
		return new ChromeDriver(chromeop);
	}
}
