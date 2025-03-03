package org.example.repository;

import org.example.modelo.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    private final List<Product> products = new ArrayList<>();

    public List<Product> findAll() {
        return products;
    }

    public Optional<Product> findById(String id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public Product save(Product product) {
        products.add(product);
        return product;
    }

    public boolean deleteById(String id) {
        return products.removeIf(p -> p.getId().equals(id));
    }
}
