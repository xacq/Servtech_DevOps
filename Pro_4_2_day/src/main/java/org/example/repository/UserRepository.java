package org.example.repository;

import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    // Derived Query: Buscar usuarios por nombre
    List<User> findByName(String name);

    // @Query con JPQL: Buscar usuarios por nombre (ignorando mayúsculas/minúsculas)
    @Query("SELECT u FROM User u WHERE LOWER(u.name) = LOWER(:name)")
    List<User> findByNameIgnoreCase(@Param("name") String name);

    // @Query con consulta nativa: Contar usuarios por nombre
    @Query(value = "SELECT COUNT(*) FROM users WHERE name = :name", nativeQuery = true)
    int countByName(@Param("name") String name);

    // @Modifying: Actualizar el nombre de un usuario por ID
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name = :name WHERE u.id = :id")
    int updateUserName(@Param("id") Long id, @Param("name") String name);

    // @Modifying: Eliminar usuarios por nombre
    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.name = :name")
    int deleteByName(@Param("name") String name);
}