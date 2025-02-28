package org.example;

public class OperacionesBancarias {

    public boolean realizarDeposito(double monto) {
        return monto > 0;
    }

    public boolean realizarRetiro(double saldo, double monto) {
        if (monto > saldo) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }
        return saldo - monto >= 0;
    }

    public boolean verificarSistemaOperativo(String sistemaOperativo) {
        return "Windows".equals(sistemaOperativo) || "Linux".equals(sistemaOperativo);
    }
}
