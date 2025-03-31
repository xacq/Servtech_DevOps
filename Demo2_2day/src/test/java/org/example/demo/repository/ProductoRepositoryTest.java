package org.example.demo.repository;
import org.example.demo.model.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)  // Opcional: evita reemplazar la DB si ya se configura H2
public class ProductoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    void testGuardarYBuscarProducto() {
        // Crea una instancia de Producto
        Producto producto = new Producto("Smartphone", 799.99);

        // Persiste la entidad utilizando TestEntityManager
        Producto productoGuardado = entityManager.persistAndFlush(producto);

        // Realiza la búsqueda usando el repositorio
        Optional<Producto> optProducto = productoRepository.findById(productoGuardado.getId());

        // Verifica que se haya encontrado y que los datos sean correctos
        assertThat(optProducto).isPresent();
        assertThat(optProducto.get().getNombre()).isEqualTo("Smartphone");
        assertThat(optProducto.get().getPrecio()).isEqualTo(799.99);
    }
}
