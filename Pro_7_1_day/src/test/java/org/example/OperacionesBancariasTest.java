package org.example;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import static org.junit.jupiter.api.Assertions.*;

class OperacionesBancariasTest {

    private final OperacionesBancarias operacionesBancarias = new OperacionesBancarias();

    @Test
    @EnabledIfSystemProperty(named = "os.name", matches = "Linux.*")
    @DisplayName("✔ Prueba solo en Linux")
    void testSoloEnLinux() {
        // Esta prueba solo se ejecutará si el sistema operativo es Linux
        assertTrue(operacionesBancarias.realizarDeposito(100), "El depósito debe ser exitoso.");
    }

    @Test
    @DisabledIfSystemProperty(named = "user.name", matches = "admin")
    @DisplayName("❌ Prueba desactivada para el usuario admin")
    void testNoEjecutarParaUsuarioAdmin() {
        // Esta prueba no se ejecutará si el nombre de usuario del sistema es 'admin'
        assertTrue(operacionesBancarias.realizarRetiro(2000, 1000), "El retiro debe ser exitoso.");
    }

    @Test
    @DisplayName("🔒 Suposición de que el sistema es Linux antes de ejecutar")
    void testSuponerLinux() {
        // Suponemos que solo se ejecuta la prueba si el sistema operativo es Linux
        Assumptions.assumeTrue(System.getProperty("os.name").contains("Linux"), "El sistema debe ser Linux");
        assertTrue(operacionesBancarias.realizarDeposito(50), "El depósito debe ser exitoso.");
    }

    @Test
    @EnabledIfSystemProperty(named = "os.name", matches = "Windows.*")
    @DisplayName("✔ Solo ejecuta si el sistema operativo es Windows")
    void testSoloEnWindows() {
        // Esta prueba solo se ejecutará si el sistema operativo es Windows
        assertTrue(operacionesBancarias.realizarRetiro(1000, 500), "El retiro debe ser exitoso.");
    }

    @Test
    @DisplayName("🔒 Test de excepción por saldo insuficiente")
    void testSaldoInsuficiente() {
        // Verificamos que se lanza una excepción si el saldo es insuficiente
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> operacionesBancarias.realizarRetiro(500, 1000));
        assertEquals("Saldo insuficiente", thrown.getMessage());
    }
}

/*
Explicación de las Anotaciones:

    @EnabledIfSystemProperty
    Permite que una prueba se ejecute solo si se cumple una condición basada en las propiedades del sistema. En este caso:
        @EnabledIfSystemProperty(named = "os.name", matches = "Linux.*"): Ejecuta el test solo si el sistema operativo es Linux.
        @EnabledIfSystemProperty(named = "os.name", matches = "Windows.*"): Ejecuta el test solo si el sistema operativo es Windows.

    @DisabledIfSystemProperty
    Desactiva la prueba si la condición especificada no se cumple. Ejemplo:
        @DisabledIfSystemProperty(named = "user.name", matches = "admin"): Desactiva la prueba si el nombre de usuario del sistema es "admin".

    assumeTrue()
    Utilizamos assumeTrue() para realizar una suposición sobre el entorno antes de ejecutar el test. Si la suposición falla, el test no se ejecuta:
        assumeTrue(System.getProperty("os.name").contains("Linux")): Solo ejecuta la prueba si el sistema operativo contiene "Linux". Si la suposición es falsa, el test se salta.

    Prueba de excepción:
        assertThrows se usa para verificar que se lance la excepción esperada, en este caso, cuando intentamos realizar un retiro con saldo insuficiente.
 */