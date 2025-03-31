package org.example.demo.integration;

import org.example.demo.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/api/products";
    }

    @Test
    void testListProducts() {
        // Realiza una petición GET al endpoint
        ResponseEntity<Product[]> response = restTemplate.getForEntity(baseUrl(), Product[].class);

        // Verifica que el estado HTTP sea 200 (OK)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verifica que se hayan retornado 2 productos
        Product[] products = response.getBody();
        assertThat(products).isNotNull();
        assertThat(products.length).isEqualTo(2);
    }

    @Test
    void testAddProduct() {
        // Crea un objeto Product sin ID
        Product newProduct = new Product(null, "Tablet", 500.0);

        // Realiza una petición POST al endpoint
        ResponseEntity<Product> response = restTemplate.postForEntity(baseUrl(), newProduct, Product.class);

        // Verifica que el estado HTTP sea 200 (OK)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verifica que el producto creado tenga un ID asignado y los datos correctos
        Product createdProduct = response.getBody();
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getId()).isNotNull();
        assertThat(createdProduct.getName()).isEqualTo("Tablet");
        assertThat(createdProduct.getPrice()).isEqualTo(500.0);
    }
}
