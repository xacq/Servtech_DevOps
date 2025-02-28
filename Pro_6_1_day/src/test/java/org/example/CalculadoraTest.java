package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import static org.junit.jupiter.api.Assertions.*;

class CalculadoraTest {

    private final Calculadora calculadora = new Calculadora();

    @Test
    @DisplayName("✔ Suma correcta entre dos números")
    void testSuma() {
        assertEquals(10, calculadora.sumar(4, 6), "La suma debería ser 10");
    }

    @Test
    @DisplayName("❌ Dividir por cero lanza excepción")
    void testDivisionPorCero() {
        assertThrows(ArithmeticException.class,
                () -> calculadora.dividir(5, 0),
                "Debe lanzar ArithmeticException al dividir por cero"
        );
    }

    @Test
    @Disabled("🚧 Esta prueba está desactivada temporalmente")
    void testMultiplicacion() {
        int resultado = 3 * 4;
        assertEquals(12, resultado, "Multiplicación de 3 x 4 debería ser 12");
    }

    @Test
    @EnabledIfSystemProperty(named = "os.name", matches = "Windows.*")
    @DisplayName("🖥️ Esta prueba solo se ejecuta en Windows")
    void testSoloEnWindows() {
        assertTrue(calculadora.esNumeroPar(4), "4 debería ser un número par");
    }

    @Test
    @DisabledIfSystemProperty(named = "user.name", matches = "admin")
    @DisplayName("🔒 No se ejecuta si el usuario es 'admin'")
    void testNoEjecutarSiEsAdmin() {
        assertFalse(calculadora.esNumeroPar(3), "3 no es un número par");
    }
}

/*
* 📌 Explicación de las anotaciones utilizadas
*     @DisplayName("✔ Suma correcta entre dos números")
*   📌 Personaliza el nombre de la prueba para que sea más descriptivo.
*     @Disabled("🚧 Esta prueba está desactivada temporalmente")
*   📌 Desactiva la prueba testMultiplicacion(), útil si estamos trabajando en otra parte del código y no queremos que falle.
*     @EnabledIfSystemProperty(named = "os.name", matches = "Windows.*")
*   📌 Solo ejecuta la prueba en Windows, ideal para pruebas específicas de sistema operativo.
* @DisabledIfSystemProperty(named = "user.name", matches = "admin")
*     📌 Desactiva la prueba si el usuario del sistema es admin, útil para evitar ciertos casos en entornos específicos.
* */