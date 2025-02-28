package org.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}