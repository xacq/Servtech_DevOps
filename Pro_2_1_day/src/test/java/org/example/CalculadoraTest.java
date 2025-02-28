package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraTest {

    @Test
    void testSumar() {
        Calculadora calc = new Calculadora();
        assertEquals(15, calc.sumar(10, 5), "La suma de 10 y 5 debería ser 15");
    }

    @Test
    void testRestar() {
        Calculadora calc = new Calculadora();
        assertEquals(5, calc.restar(10, 5), "La resta de 10 y 5 debería ser 5");
    }

    @Test
    void testMultiplicar() {
        Calculadora calc = new Calculadora();
        assertEquals(50, calc.multiplicar(10, 5), "La multiplicación de 10 y 5 debería ser 50");
    }

    @Test
    void testDividir() {
        Calculadora calc = new Calculadora();
        assertEquals(2.0, calc.dividir(10, 5), "La división de 10 entre 5 debería ser 2.0");
    }

    @Test
    void testDividirPorCero() {
        Calculadora calc = new Calculadora();
        Exception exception = assertThrows(ArithmeticException.class, () -> calc.dividir(10, 0));
        assertEquals("No se puede dividir entre cero", exception.getMessage());
    }
}
