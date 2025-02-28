package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;

import java.util.List;

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

    /*@Test
    void testRetiroExcedeSaldo() {
        assertThrows(IllegalArgumentException.class, () -> cuenta.retirar(2000.0),
                "Debe lanzar excepción cuando el retiro excede el saldo disponible");
    }*/

    @Test
    void testRetiroExcedeSaldo() {
        assertThrows(SaldoInsuficienteException.class, () -> cuenta.retirar(2000.0),
                "Debe lanzar SaldoInsuficienteException cuando el retiro excede el saldo disponible");
    }

    @Test
    void testHistorialTransacciones() {
        cuenta.depositar(500.0);
        cuenta.retirar(200.0);

        List<String> historial = cuenta.getHistorialTransacciones();

        assertEquals(3, historial.size(), "El historial debe contener 3 transacciones (incluyendo la inicial)");
        assertEquals("Depósito de 500.0. Nuevo saldo: 1500.0", historial.get(1));
        assertEquals("Retiro de 200.0. Nuevo saldo: 1300.0", historial.get(2));
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
 Estas refactorizaciones mejoran la calidad del código sin alterar la lógica central:

 Excepción personalizada (SaldoInsuficienteException) → hace que los errores sean más descriptivos.
 Historial de transacciones → permite auditar operaciones.

 Así aplicamos TDD + Clean Code para mejorar un sistema bancario básico. 🚀
 * **/