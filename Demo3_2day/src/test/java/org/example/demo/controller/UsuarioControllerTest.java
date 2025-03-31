package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.UsuarioService;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Inyecta un mock del servicio para aislar el controlador
    @MockBean
    private UsuarioService usuarioService;

    // Utilidad para convertir objetos a JSON
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListarUsuarios() throws Exception {
        List<Usuario> usuarios = Arrays.asList(
                new Usuario(1L, "Juan Pérez", "juan@example.com"),
                new Usuario(2L, "María García", "maria@example.com")
        );
        when(usuarioService.obtenerUsuarios()).thenReturn(usuarios);

        mockMvc.perform(get("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$[1].email", is("maria@example.com")));
    }

    @Test
    void testCrearUsuario() throws Exception {
        Usuario usuarioNuevo = new Usuario(null, "Carlos López", "carlos@example.com");
        Usuario usuarioCreado = new Usuario(3L, "Carlos López", "carlos@example.com");

        when(usuarioService.crearUsuario(usuarioNuevo)).thenReturn(usuarioCreado);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioNuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.nombre", is("Carlos López")))
                .andExpect(jsonPath("$.email", is("carlos@example.com")));
    }

    @Test
    void testDeleteUsuario() throws Exception {
        Long id = 1L;
        doNothing().when(usuarioService).deleteUsuario(id);
        mockMvc.perform(delete("/api/usuarios/{id}",id)).andExpect(status().isNoContent());
        verify(usuarioService, times(1)).deleteUsuario(id);
    }
}
