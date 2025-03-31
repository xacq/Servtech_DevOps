package dev.magadiflo.app.model.dto;

import java.math.BigDecimal;

public record Transaction(Long bankId, Long sourceAccountId, Long targetAccountId, BigDecimal amount) {
}
