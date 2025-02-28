package org.example;

import java.util.ArrayList;
import java.util.List;

public class CuentaBancaria {
    private double saldo;

    public CuentaBancaria(double saldoInicial) {
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo");
        }
        this.saldo = saldoInicial;
    }

    public double getSaldo() {
        return saldo;
    }

    public void depositar(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto del depósito debe ser positivo");
        }
        saldo += monto;
    }

    public void retirar(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto del retiro debe ser positivo");
        }
        if (monto > saldo) {
            throw new SaldoInsuficienteException("Saldo insuficiente para retirar: " + monto);
        }
        saldo -= monto;
    }
}
