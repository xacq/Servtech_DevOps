package org.example.demo.service;

import org.example.demo.model.Producto;
import org.example.demo.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void testListarProductos() {
        // Configuración del mock
        Producto prod1 = new Producto("Producto1", 10.0);
        Producto prod2 = new Producto("Producto2", 20.0);
        List<Producto> listaMock = Arrays.asList(prod1, prod2);

        when(productoRepository.findAll()).thenReturn(listaMock);

        // Ejecución del método a probar
        List<Producto> resultado = productoService.listarProductos();

        // Verificación
        assertEquals(2, resultado.size());
        assertEquals("Producto1", resultado.get(0).getNombre());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testGuardarProducto() {
        Producto producto = new Producto("Producto3", 30.0);
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto guardado = productoService.guardarProducto(producto);
        assertNotNull(guardado);
        assertEquals("Producto3", guardado.getNombre());
        verify(productoRepository, times(1)).save(producto);
    }
}
