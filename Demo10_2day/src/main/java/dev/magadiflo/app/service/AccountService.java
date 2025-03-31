package dev.magadiflo.app.service;

import dev.magadiflo.app.model.entity.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> findAll();

    Optional<Account> findById(Long id);

    BigDecimal reviewBalance(Long accountId);

    int reviewTotalTransfers(Long bankId);

    Account save(Account account);

    void transfer(Long bankId, Long sourceAccountId, Long targetAccountId, BigDecimal amount);

    Optional<Boolean> deleteAccountById(Long id);
}
