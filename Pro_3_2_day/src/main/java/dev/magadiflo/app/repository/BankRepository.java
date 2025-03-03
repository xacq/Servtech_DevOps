package dev.magadiflo.app.repository;

import dev.magadiflo.app.model.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {
}
