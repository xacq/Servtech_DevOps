package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BancoTest {

    @Mock
    private ServicioBancario servicioBancario;

    @Test
    public void testRealizarTransferencia() {
        // Datos de prueba
        String cuentaOrigen = "12345";
        String cuentaDestino = "67890";
        double monto = 1000.0;

        // Simulamos el comportamiento del mock
        when(servicioBancario.transferir(cuentaOrigen, cuentaDestino, monto)).thenReturn(true);

        // Creamos la instancia de Banco manualmente con el mock
        Banco banco = new Banco(servicioBancario);

        // Ejecutamos el método a probar
        boolean resultado = banco.realizarTransferencia(cuentaOrigen, cuentaDestino, monto);

        // Verificamos que el resultado sea el esperado
        assertTrue(resultado);

        // Verificamos que el método transferir del mock fue llamado con los parámetros correctos
        verify(servicioBancario).transferir(cuentaOrigen, cuentaDestino, monto);
    }
}
