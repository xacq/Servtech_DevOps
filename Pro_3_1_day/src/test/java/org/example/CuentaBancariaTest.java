package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;

class CuentaBancariaTest {

    private CuentaBancaria cuenta;
    //Accion a generar antes de cada TEST
    @BeforeEach
    void setUp() {
        cuenta = new CuentaBancaria(1000.0); // Se inicia con 1000 de saldo
    }

    @Test
    void testDeposito() {
        cuenta.depositar(500.0);
        assertEquals(1500.0, cuenta.getSaldo(), "El saldo debe ser 1500 después del depósito");
    }

    @Test
    void testRetiroExitoso() {
        cuenta.retirar(300.0);
        assertEquals(700.0, cuenta.getSaldo(), "El saldo debe ser 700 después del retiro de 300");
    }

    @Test
    void testRetiroExcedeSaldo() {
        assertThrows(IllegalArgumentException.class, () -> cuenta.retirar(2000.0),
                "Debe lanzar excepción cuando el retiro excede el saldo disponible");
    }
    //Accion despues de cada TEST
    @AfterEach
    void tearDown() {
        System.out.println("Prueba completada.");
    }
    //Accion Antes de todo el TEST
    @BeforeAll
    static void initAll() {
        System.out.println("Iniciando pruebas de Cuenta Bancaria...");
    }
    //Accion despues de todo el TEST
    @AfterAll
    static void cleanupAll() {
        System.out.println("Todas las pruebas completadas.");
    }
}

/**
 * JUnit 5 Annotations en CuentaBancariaTest:
 *
 *     @BeforeEach: Crea una nueva cuenta antes de cada prueba.
 *     @Test: Define las pruebas unitarias.
 *     @AfterEach: Mensaje de finalización tras cada prueba.
 *     @BeforeAll: Mensaje antes de todas las pruebas.
 *     @AfterAll: Mensaje después de todas las pruebas.
 *
 * Pruebas Implementadas:
 *
 *     ✅ Depositar dinero y verificar que el saldo aumente correctamente.
 *     ✅ Retirar dinero exitosamente.
 *     ✅ Intentar retirar más dinero del disponible y lanzar una excepción.
 *
 * Uso de assertEquals y assertThrows:
 *
 *     assertEquals(expected, actual): Verifica el saldo después de una operación.
 *     assertThrows(Exception.class, () -> método): Verifica que lanzar una excepción si hay un error.
 * **/