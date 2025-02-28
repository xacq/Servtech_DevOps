package org.example.model.service;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.example.model.Usuario;
import org.example.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @Test
    void testObtenerUsuarioExternoMockeado() {
        // Configurar mock de API externa
        wireMock.stubFor(get(urlEqualTo("/api/usuarios/123"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                        {
                            "id": 123,
                            "nombre": "Usuario Mockeado",
                            "email": "mock@test.com"
                        }
                        """)));

        String apiUrl = wireMock.baseUrl() + "/api/usuarios/123";

        Usuario resultado = usuarioService.obtenerUsuarioExterno(apiUrl);

        assertNotNull(resultado);
        assertEquals(123L, resultado.getId());
        assertEquals("Usuario Mockeado", resultado.getNombre());
    }

    @Test
    void testGuardarUsuarioLocal() {
        Usuario nuevoUsuario = new Usuario("Test", "test@example.com");

        Usuario guardado = usuarioService.guardarUsuario(nuevoUsuario);

        assertNotNull(guardado.getId());
        assertEquals("Test", guardado.getNombre());

        Usuario encontrado = usuarioService.obtenerUsuarioLocal(guardado.getId());
        assertEquals(guardado.getEmail(), encontrado.getEmail());
    }
}