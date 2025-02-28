package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;
import org.apache.commons.io.FileUtils; // Asegúrate de tener esta dependencia en tu pom.xml
import java.io.IOException;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.PageLoadStrategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocatorExampleTest {

    private WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().driverVersion("133.0.6943.142").setup();
    }

    @BeforeEach
    void setupTest() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--disable-extensions",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--headless"  // Opcional: para ejecución sin UI
        );
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testLocalizadores() {
        try {
            WebElement usernameField = driver.findElement(By.id("user-name"));
            usernameField.sendKeys("standard_user");

            WebElement passwordField = driver.findElement(By.name("password"));
            passwordField.sendKeys("secret_sauce");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement loginButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.cssSelector("#login-button"))
            );
            loginButton.click();

            // Esperar a que cargue la página siguiente
            wait.until(ExpectedConditions.urlContains("/inventory.html"));

            // Verificar título de la página
            String pageTitle = driver.getTitle();
            assertEquals("Swag Labs", pageTitle);

        } catch (Exception e) {
            // Captura de pantalla
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(screenshotFile, new File("screenshot.png"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            throw e;
        }
    }
}

/*
* Explicación de los Localizadores:
    By.id("user-name"): Localiza un elemento por su atributo id.
    By.name("password"): Localiza un elemento por su atributo name.
    By.cssSelector("#login-button"): Localiza un elemento usando un selector CSS. El # representa el id del elemento.
    By.xpath("//h3[@data-test='error']"): Localiza un elemento usando una expresión XPath. //h3 busca cualquier etiqueta h3 en la página. [@data-test='error'] filtra los h3 para seleccionar solo aquellos que tienen un atributo data-test con el valor "error".
* */