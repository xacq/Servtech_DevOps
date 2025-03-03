package org.example.controller;

import org.example.modelo.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    ProductControllerTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product(UUID.randomUUID().toString(), "Laptop", 1200.0);

        // Insertamos el producto de prueba
        webTestClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testProduct)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testListProducts() {
        webTestClient.get()
                .uri("/api/products")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Product.class)
                .value(products -> assertThat(products).isNotEmpty());
    }

    @Test
    void testGetProductById() {
        webTestClient.get()
                .uri("/api/products/" + testProduct.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .value(product -> assertThat(product.getName()).isEqualTo("Laptop"));
    }

    @Test
    void testCreateProduct() {
        Product newProduct = new Product(UUID.randomUUID().toString(), "Mouse", 25.0);

        webTestClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newProduct)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .value(product -> assertThat(product.getName()).isEqualTo("Mouse"));
    }

    @Test
    void testDeleteProduct() {
        webTestClient.delete()
                .uri("/api/products/" + testProduct.getId())
                .exchange()
                .expectStatus().isNoContent();

        // Verificar que ya no existe
        webTestClient.get()
                .uri("/api/products/" + testProduct.getId())
                .exchange()
                .expectStatus().isNotFound();
    }
}
