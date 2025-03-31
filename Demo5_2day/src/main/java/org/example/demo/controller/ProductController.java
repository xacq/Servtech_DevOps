package org.example.demo.controller;

import org.example.demo.model.Product;
import org.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Endpoint para listar productos
    @GetMapping
    public List<Product> listProducts() {
        return productService.getAllProducts();
    }

    // Endpoint para crear un nuevo producto
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }
}
