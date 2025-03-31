package org.example.demo.controller;

import org.example.demo.model.Producto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.util.Arrays;
import java.util.List;

@RestController
public class ProductoController {

    @GetMapping("/api/productos")
    public Flux<Producto> getProductos() {
        List<Producto> productos = Arrays.asList(
                new Producto(1L, "Laptop", 1200.0),
                new Producto(2L, "Smartphone", 800.0)
        );
        return Flux.fromIterable(productos);
    }
}
