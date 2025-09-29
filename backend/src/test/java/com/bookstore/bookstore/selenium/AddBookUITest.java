package com.bookstore.bookstore.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddBookUITest {
    private WebDriver driver;

    @BeforeEach
    void setup() {
        // Setup ChromeDriver
        WebDriverManager.chromedriver().setup();

        // Configure ChromeOptions for CI
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // headless mode
        options.addArguments("--no-sandbox"); 
        options.addArguments("--disable-dev-shm-usage"); 
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*"); // required for newer Chrome versions
        options.addArguments("--user-data-dir=/tmp/unique-dir-" + UUID.randomUUID()); // unique dir

        this.driver = new ChromeDriver(options);

        // Open the web app
        this.driver.get("http://localhost:5173");
    }

    @Test
    void addBookTest() {
        // Fill book details
        this.driver.findElement(By.id("title")).sendKeys("The Hobbit");
        this.driver.findElement(By.id("author")).sendKeys("Tolkien");
        this.driver.findElement(By.id("price")).sendKeys("12.99");

        // Click add button
        this.driver.findElement(By.id("addButton")).click();

        // Wait for the added book to appear
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        WebElement addedBook = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[contains(text(),'The Hobbit')]")
        ));

        // Assert the book was added
        Assertions.assertNotNull(addedBook);
    }

    @AfterEach
    void teardown() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }
}
