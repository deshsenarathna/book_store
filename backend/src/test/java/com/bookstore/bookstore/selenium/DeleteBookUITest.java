package com.bookstore.bookstore.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeleteBookUITest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        
        // Configure ChromeOptions for CI/headless mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // headless mode
        options.addArguments("--no-sandbox"); 
        options.addArguments("--disable-dev-shm-usage"); 
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--user-data-dir=/tmp/unique-dir-" + java.util.UUID.randomUUID());
        
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testDeleteBook() {
        driver.get("http://localhost:5175"); // React app URL

        // Wait for the first Delete button to appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Delete']")));

        // Find all Delete buttons before deletion
        List<WebElement> deleteButtonsBefore = driver.findElements(By.xpath("//button[text()='Delete']"));
        int initialCount = deleteButtonsBefore.size();
        assertTrue(initialCount > 0, "No books to delete!");

        // Click the first Delete button
        deleteButtonsBefore.get(0).click();

        // Handle confirmation popup
        driver.switchTo().alert().accept();

        // Wait until the number of Delete buttons decreases
        wait.until(driver -> driver.findElements(By.xpath("//button[text()='Delete']")).size() == initialCount - 1);

        // Verify one book is deleted
        List<WebElement> deleteButtonsAfter = driver.findElements(By.xpath("//button[text()='Delete']"));
        int newCount = deleteButtonsAfter.size();
        assertTrue(newCount == initialCount - 1, "Book was not deleted");
    }
}
