package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import static org.junit.Assert.*;

public class LoginSteps {

    private WebDriver driver;

    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
    }

    @Dado("Estoy en la página de login de Saucedemo")
    public void navegarAPaginaLogin() {
        driver.get("https://www.saucedemo.com/");
    }

    @Cuando("Ingreso el usuario {string}")
    public void ingresarUsuario(String usuario) {
        driver.findElement(By.id("user-name")).sendKeys(usuario);
    }

    @Cuando("Ingreso la contraseña {string}")
    public void ingresarContrasena(String contrasena) {
        driver.findElement(By.id("password")).sendKeys(contrasena);
    }

    @Cuando("Hago clic en el botón de login")
    public void hacerClicLogin() {
        driver.findElement(By.id("login-button")).click();
    }

    @Entonces("Debo ver la página de inventario")
    public void verificarPaginaInventario() {
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/inventory.html"));
    }

    @Entonces("Debo ver el título {string}")
    public void verificarTitulo(String tituloEsperado) {
        assertEquals(tituloEsperado, driver.getTitle());
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}