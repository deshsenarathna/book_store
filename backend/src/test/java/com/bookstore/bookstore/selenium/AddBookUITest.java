package com.bookstore.bookstore.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AddBookUITest {

    private WebDriver driver;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("http://localhost:5175"); // your React UI URL
    }

    @Test
    void addBookTest() {
        driver.findElement(By.id("title")).sendKeys("The Hobbit");
        driver.findElement(By.id("author")).sendKeys("Tolkien");
        driver.findElement(By.id("price")).sendKeys("12.99");
        driver.findElement(By.id("addButton")).click();

        // Wait for the new book to appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement addedBook = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(text(),'The Hobbit')]")
        ));

        Assertions.assertNotNull(addedBook);
    }
    @AfterEach
    void teardown() {
        if (driver != null) driver.quit();
    }
}
