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

    /**
    //VARIACIONES DE FUNCIONALIDAD assertThrows
    @Test
        void testRetiroExcedeSaldo() {
            CuentaBancaria cuenta = new CuentaBancaria(500.0);
            assertThrows(SaldoInsuficienteException.class, () -> cuenta.retirar(1000.0));
        }
    **/

    @Test
    void testRetiroExcedeSaldo() {
        CuentaBancaria cuenta = new CuentaBancaria(500.0);

        // Verifica que lanzar una excepción cuando se intenta retirar más de lo que hay en saldo
        SaldoInsuficienteException excepcion = assertThrows(SaldoInsuficienteException.class,
                () -> cuenta.retirar(1000.0),
                "Debe lanzar SaldoInsuficienteException cuando el retiro excede el saldo disponible"
        );

        // Verifica que el mensaje de la excepción es correcto
        assertEquals("Saldo insuficiente para retirar: 1000.0", excepcion.getMessage());
    }

    //Validar IllegalArgumentException para un Monto Negativo
    @Test
    void testRetiroMontoNegativo() {
        CuentaBancaria cuenta = new CuentaBancaria(500.0);

        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class,
                () -> cuenta.retirar(-100.0),
                "Debe lanzar IllegalArgumentException cuando el monto del retiro es negativo"
        );

        assertEquals("El monto del retiro debe ser positivo", excepcion.getMessage());
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