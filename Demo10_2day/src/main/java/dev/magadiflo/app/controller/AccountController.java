package dev.magadiflo.app.controller;

import dev.magadiflo.app.model.dto.Transaction;
import dev.magadiflo.app.model.entity.Account;
import dev.magadiflo.app.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<Account>> findAllAccounts() {
        return ResponseEntity.ok(this.accountService.findAll());
    }

    @GetMapping(path = "/{accountId}")
    public ResponseEntity<Account> findAccount(@PathVariable Long accountId) {
        return this.accountService.findById(accountId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Account> saveAccount(@RequestBody Account account) {
        Account accountDB = this.accountService.save(account);
        URI accountURI = URI.create("/api/v1/accounts/" + accountDB.getId());
        return ResponseEntity.created(accountURI).body(accountDB);
    }

    @PostMapping(path = "/transfer")
    public ResponseEntity<?> transfer(@RequestBody Transaction transaction) {
        this.accountService.transfer(transaction.bankId(), transaction.sourceAccountId(), transaction.targetAccountId(), transaction.amount());
        Map<String, Object> response = new HashMap<>();
        response.put("datetime", LocalDateTime.now());
        response.put("status", HttpStatus.CREATED);
        response.put("code", HttpStatus.CREATED.value());
        response.put("message", "Transferencia exitosa");
        response.put("transaction", transaction);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        return this.accountService.deleteAccountById(id)
                .map(wasDeleted -> ResponseEntity.noContent().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
