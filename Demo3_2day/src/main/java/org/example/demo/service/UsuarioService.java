package org.example.demo.service;

import org.example.demo.model.Usuario;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    public List<Usuario> obtenerUsuarios() {
        // Retorna una lista estática de usuarios para ejemplo
        return Arrays.asList(
                new Usuario(1L, "Juan Pérez", "juan@example.com"),
                new Usuario(2L, "María García", "maria@example.com")
        );
    }

    public Usuario crearUsuario(Usuario usuario) {
        // Simula la creación asignándole un ID
        usuario.setId(3L);
        return usuario;
    }
}
