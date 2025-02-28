package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TitleVerificationTest {

    private WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup(); // Configura ChromeDriver con WebDriverManager
    }

    @BeforeEach
    void setupTest() {
        driver = new ChromeDriver(); // Inicializa ChromeDriver
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit(); // Cierra el navegador después de cada prueba
        }
    }

    @Test
    void testVerificarTituloPagina() {
        String url = "https://www.example.com";
        driver.get(url); // Abre la página web

        String expectedTitle = "Example Domain";
        String actualTitle = driver.getTitle(); // Obtiene el título de la página

        assertEquals(expectedTitle, actualTitle, "El título de la página no es el esperado.");
    }
}

/*
* Explicación del Código:

    WebDriverManager.chromedriver().setup();: (Dentro de @BeforeAll) Este código configura el ChromeDriver usando WebDriverManager. WebDriverManager descargará automáticamente la versión correcta del driver de Chrome compatible con tu navegador Chrome instalado. Esto solo se hace una vez antes de todas las pruebas.

    driver = new ChromeDriver();: (Dentro de @BeforeEach) Crea una nueva instancia del ChromeDriver para cada prueba. Esto abre una nueva ventana de Chrome cada vez que se ejecuta una prueba.

    driver.get(url);: Abre la URL especificada en el navegador.

    driver.getTitle();: Obtiene el título de la página web actual.

    assertEquals(expectedTitle, actualTitle, "El título de la página no es el esperado.");: Verifica que el título actual de la página coincida con el título esperado. Si no coinciden, la prueba fallará.

    driver.quit();: (Dentro de @AfterEach) Cierra el navegador después de cada prueba. Es importante cerrar el navegador al finalizar cada prueba para liberar recursos y evitar problemas en pruebas posteriores.
* */