package dev.magadiflo.app.integrationTest.repository;

import dev.magadiflo.app.model.entity.Account;
import dev.magadiflo.app.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldFindAnAccountById() {
        // given
        Long accountId = 1L;

        // when
        Optional<Account> accountOptional = this.accountRepository.findById(accountId);

        // then
        assertTrue(accountOptional.isPresent());
        assertEquals(1L, accountOptional.get().getId());
        assertEquals("Martín", accountOptional.get().getPerson());
        assertEquals(2000D, accountOptional.get().getBalance().doubleValue());
    }

    @Test
    void shouldFindAnAccountByPerson() {
        // given
        String person = "Martín";

        // when
        Optional<Account> accountOptional = this.accountRepository.findAccountByPerson(person);

        // then
        assertTrue(accountOptional.isPresent());
        assertEquals(1L, accountOptional.get().getId());
        assertEquals("Martín", accountOptional.get().getPerson());
        assertEquals(2000D, accountOptional.get().getBalance().doubleValue());
    }

    @Test
    void shouldReturnAnEmptyOptionalWhenAAccountDoesNotExistSearchedForByPerson() {
        // given
        String person = "Ronaldo";

        // when
        Optional<Account> accountOptional = this.accountRepository.findAccountByPerson(person);

        // then
        assertTrue(accountOptional.isEmpty());
    }

    @Test
    void shouldSaveAnAccount() {
        // given
        Account accountToSave = Account.builder()
                .person("Eli")
                .balance(new BigDecimal("2500"))
                .build();

        // when
        Integer affectedRows = this.accountRepository.saveAccount(accountToSave);
        Optional<Account> accountOptional = this.accountRepository.findById(3L);

        // then
        assertNotNull(affectedRows);
        assertEquals(1, affectedRows);
        assertTrue(accountOptional.isPresent());
        assertEquals(3L, accountOptional.get().getId());
        assertEquals(accountToSave.getPerson(), accountOptional.get().getPerson());
        assertEquals(accountToSave.getBalance().doubleValue(), accountOptional.get().getBalance().doubleValue());
    }

    @Test
    void shouldUpdateAnAccount() {
        // given
        Account accountToUpdate = Account.builder()
                .id(1L)
                .person("Gaspar")
                .balance(new BigDecimal("5000"))
                .build();

        // when
        Integer affectedRows = this.accountRepository.updateAccount(accountToUpdate);
        Optional<Account> accountOptional = this.accountRepository.findById(1L);

        // then
        assertNotNull(affectedRows);
        assertEquals(1, affectedRows);
        assertTrue(accountOptional.isPresent());
        assertEquals(accountToUpdate.getPerson(), accountOptional.get().getPerson());
        assertEquals(accountToUpdate.getBalance().doubleValue(), accountOptional.get().getBalance().doubleValue());
    }

    @Test
    void shouldDeletedAnAccount() {
        // given
        Long accountId = 1L;

        // when
        Integer affectedRows = this.accountRepository.deleteAccountById(accountId);
        Optional<Account> accountOptional = this.accountRepository.findById(accountId);

        // then
        assertNotNull(affectedRows);
        assertEquals(1, affectedRows);
        assertTrue(accountOptional.isEmpty());
    }
}