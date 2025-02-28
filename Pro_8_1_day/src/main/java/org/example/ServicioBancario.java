package org.example;

public interface ServicioBancario {
    boolean transferir(String cuentaOrigen, String cuentaDestino, double monto);
}
