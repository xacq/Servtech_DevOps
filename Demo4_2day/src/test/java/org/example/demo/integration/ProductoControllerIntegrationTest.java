package org.example.demo.integration;

import org.example.demo.model.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductoControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testGetProductos() {
        webTestClient.get().uri("/api/productos")
                .exchange() // Realiza la llamada real al endpoint
                .expectStatus().isOk()
                .expectBodyList(Producto.class)
                .hasSize(2)
                .consumeWith(response -> {
                    // Verifica detalles de la respuesta (por ejemplo, el primer producto)
                    Producto primerProducto = response.getResponseBody().get(0);
                    assert(primerProducto.getNombre().equals("Laptop"));
                });
    }
}
