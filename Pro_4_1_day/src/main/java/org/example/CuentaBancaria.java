package org.example;

import java.util.ArrayList;
import java.util.List;

public class CuentaBancaria {
    private double saldo;
    private List<String> historialTransacciones;

    public CuentaBancaria(double saldoInicial) {
        this.saldo = saldoInicial;
        this.historialTransacciones = new ArrayList<>();
        registrarTransaccion("Cuenta creada con saldo inicial de " + saldoInicial);
    }

    public double getSaldo() {
        return saldo;
    }

    public List<String> getHistorialTransacciones() {
        return historialTransacciones;
    }

    public void depositar(double monto) {
        validarMontoPositivo(monto);
        saldo += monto;
        registrarTransaccion("Depósito de " + monto + ". Nuevo saldo: " + saldo);
    }

    public void retirar(double monto) {
        validarMontoPositivo(monto);
        if (monto > saldo) {
            throw new SaldoInsuficienteException("Saldo insuficiente para retirar: " + monto);
        }
        saldo -= monto;
        registrarTransaccion("Retiro de " + monto + ". Nuevo saldo: " + saldo);
    }

    private void validarMontoPositivo(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
    }

    private void registrarTransaccion(String mensaje) {
        historialTransacciones.add(mensaje);
    }
}
