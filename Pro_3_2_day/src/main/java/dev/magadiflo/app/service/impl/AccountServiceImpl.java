package dev.magadiflo.app.service.impl;

import dev.magadiflo.app.exception.NotFoundEntity;
import dev.magadiflo.app.model.entity.Account;
import dev.magadiflo.app.model.entity.Bank;
import dev.magadiflo.app.repository.AccountRepository;
import dev.magadiflo.app.repository.BankRepository;
import dev.magadiflo.app.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return this.accountRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Account> findById(Long id) {
        return this.accountRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal reviewBalance(Long accountId) {
        return this.accountRepository.findById(accountId)
                .map(Account::getBalance)
                .orElseThrow(() -> new NotFoundEntity("No existe la cuenta con el id " + accountId));
    }

    @Override
    @Transactional(readOnly = true)
    public int reviewTotalTransfers(Long bankId) {
        return this.bankRepository.findById(bankId)
                .map(Bank::getTotalTransfers)
                .orElseThrow(() -> new NotFoundEntity("No existe el banco con el id " + bankId));
    }

    @Override
    @Transactional
    public Account save(Account account) {
        return this.accountRepository.save(account);
    }

    @Override
    @Transactional
    public void transfer(Long bankId, Long sourceAccountId, Long targetAccountId, BigDecimal amount) {
        Account sourceAccount = this.accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new NotFoundEntity("No existe la cuenta de origen con id " + sourceAccountId));
        Account targetAccount = this.accountRepository.findById(targetAccountId)
                .orElseThrow(() -> new NotFoundEntity("No existe la cuenta de destino con id " + targetAccountId));
        Bank bank = this.bankRepository.findById(bankId)
                .orElseThrow(() -> new NotFoundEntity("No existe el banco con el id " + bankId));

        sourceAccount.debit(amount);
        targetAccount.credit(amount);
        bank.setTotalTransfers(bank.getTotalTransfers() + 1);

        this.accountRepository.save(sourceAccount);
        this.accountRepository.save(targetAccount);
        this.bankRepository.save(bank);
    }

    @Override
    @Transactional
    public Optional<Boolean> deleteAccountById(Long id) {
        return this.accountRepository.findById(id)
                .map(accountDB -> {
                    this.accountRepository.deleteAccountById(accountDB.getId());
                    return true;
                });
    }
}
