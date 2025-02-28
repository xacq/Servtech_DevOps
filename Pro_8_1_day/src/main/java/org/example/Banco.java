package org.example;

public class Banco {
    private final ServicioBancario servicioBancario;

    public Banco(ServicioBancario servicioBancario) {
        this.servicioBancario = servicioBancario;
    }

    public boolean realizarTransferencia(String cuentaOrigen, String cuentaDestino, double monto) {
        return servicioBancario.transferir(cuentaOrigen, cuentaDestino, monto);
    }
}

