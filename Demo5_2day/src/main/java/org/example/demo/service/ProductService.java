package org.example.demo.service;

import org.example.demo.model.Product;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    public List<Product> getAllProducts() {
        return Arrays.asList(
                new Product(1L, "Laptop", 1200.0),
                new Product(2L, "Smartphone", 800.0)
        );
    }

    public Product createProduct(Product product) {
        // Simula la creación asignando un ID (en un caso real, se persistiría en base de datos)
        product.setId(3L);
        return product;
    }
}
