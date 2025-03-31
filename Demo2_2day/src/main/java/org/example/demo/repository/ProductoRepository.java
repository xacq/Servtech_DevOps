package org.example.demo.repository;
import org.example.demo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Se pueden agregar métodos de consulta personalizados si es necesario
}
