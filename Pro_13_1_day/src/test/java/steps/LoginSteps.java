package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginSteps {
    private WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "ruta/del/chromedriver"); // Ajusta la ruta de ChromeDriver
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Given("el usuario está en la página de login")
    public void usuarioEnLogin() {
        driver.get("https://www.ejemplo.com/login"); // Reemplaza con la URL real
    }

    @When("ingresa {string} en el campo usuario")
    public void ingresarUsuario(String usuario) {
        driver.findElement(By.id("username")).sendKeys(usuario);
    }

    @When("ingresa {string} en el campo contraseña")
    public void ingresarContraseña(String contraseña) {
        driver.findElement(By.id("password")).sendKeys(contraseña);
    }

    @When("hace clic en el botón de login")
    public void hacerClicEnLogin() {
        driver.findElement(By.id("loginButton")).click();
    }

    @Then("debería ver el mensaje {string}")
    public void verificarMensaje(String mensajeEsperado) {
        String mensajeReal = driver.findElement(By.id("welcomeMessage")).getText();
        Assert.assertEquals(mensajeEsperado, mensajeReal);
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
