package org.example.demo.controller;

import org.example.demo.model.Producto;
import org.example.demo.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Endpoint para listar productos
    @GetMapping
    public List<Producto> getProductos() {
        return productoService.listarProductos();
    }

    // Endpoint para crear un nuevo producto
    @PostMapping
    public Producto createProducto(@RequestBody Producto producto) {
        return productoService.guardarProducto(producto);
    }
}
