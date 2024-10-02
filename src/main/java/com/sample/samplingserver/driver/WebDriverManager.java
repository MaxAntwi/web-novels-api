package com.sample.samplingserver.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverManager {
    private static WebDriver driver;

    // Private constructor to prevent instantiation
    private WebDriverManager() {}

    // Get the WebDriver instance
    public static WebDriver getDriver() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--enable-lazy-load-images");
            options.addArguments("--disable-images", "--blink-settings=imagesEnabled=false");
            options.addArguments("--enable-lazy-load-scripts");
            options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

            // Create the WebDriver instance if it's not already created
            driver = new ChromeDriver(options);
        }
        return driver;
    }

    // Shutdown the WebDriver instance gracefully
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
