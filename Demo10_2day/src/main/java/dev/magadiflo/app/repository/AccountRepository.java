package dev.magadiflo.app.repository;

import dev.magadiflo.app.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByPerson(String person);

    @Query(value = """
            SELECT a
            FROM Account AS a
            WHERE a.person = :person
            """)
    Optional<Account> findAccountByPerson(String person);

    @Modifying
    @Query(value = """
            INSERT INTO accounts(person, balance)
            VALUES(:#{#account.getPerson()}, :#{#account.getBalance()})
            """, nativeQuery = true)
    Integer saveAccount(Account account);

    @Modifying
    @Query(value = """
            UPDATE accounts
            SET person = :#{#account.getPerson()},
                balance = :#{#account.getBalance()}
            WHERE id = :#{#account.getId()}
            """, nativeQuery = true)
    Integer updateAccount(Account account);

    @Modifying
    @Query(value = """
            DELETE FROM accounts
            WHERE id = :accountId
            """, nativeQuery = true)
    Integer deleteAccountById(Long accountId);
}
