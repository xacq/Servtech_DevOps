import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraTest {

    @Test
    void testSuma() {
        Calculadora calc = new Calculadora();
        assertEquals(5, calc.sumar(2, 3), "La suma de 2 y 3 debería ser 5");
    }

    @Test
    void testResta() {
        Calculadora calc = new Calculadora();
        assertEquals(1, calc.restar(3, 2), "La resta de 3 y 2 debería ser 1");
    }
}
