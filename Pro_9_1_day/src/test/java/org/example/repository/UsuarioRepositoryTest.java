package org.example.repository;

import org.example.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        // Datos de prueba iniciales
        usuarioRepository.save(new Usuario("Ana", "ana@example.com"));
        usuarioRepository.save(new Usuario("Carlos", "carlos@example.com"));
    }

    @Test
    void testBuscarUsuarioPorEmail() {
        Usuario usuario = usuarioRepository.findAll().stream()
                .filter(u -> u.getEmail().equals("ana@example.com"))
                .findFirst()
                .orElse(null);

        assertNotNull(usuario);
        assertEquals("Ana", usuario.getNombre());
    }

    @Test
    void testGuardarUsuario() {
        Usuario nuevoUsuario = new Usuario("Luisa", "luisa@example.com");
        Usuario guardado = usuarioRepository.save(nuevoUsuario);

        assertNotNull(guardado.getId());
        assertEquals("Luisa", guardado.getNombre());
    }
}