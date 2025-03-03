package org.example.repository;

import org.example.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByName() {
        // Given
        User user1 = new User();
        user1.setName("John Doe");
        entityManager.persist(user1);

        User user2 = new User();
        user2.setName("Jane Doe");
        entityManager.persist(user2);

        entityManager.flush();

        // When
        List<User> users = userRepository.findByName("John Doe");

        // Then
        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());
    }

    @Test
    void testFindByNameIgnoreCase() {
        // Given
        User user = new User();
        user.setName("John Doe");
        entityManager.persist(user);
        entityManager.flush();

        // When
        List<User> users = userRepository.findByNameIgnoreCase("john doe");

        // Then
        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());
    }

    @Test
    void testCountByName() {
        // Given
        User user1 = new User();
        user1.setName("John Doe");
        entityManager.persist(user1);

        User user2 = new User();
        user2.setName("John Doe");
        entityManager.persist(user2);

        entityManager.flush();

        // When
        int count = userRepository.countByName("John Doe");

        // Then
        assertEquals(2, count);
    }

    @Test
    void testUpdateUserName() {
        // Given
        User user = new User();
        user.setName("John Doe");
        entityManager.persist(user);
        entityManager.flush();

        // When
        int updatedRows = userRepository.updateUserName(user.getId(), "Jane Doe");

        // Then
        assertEquals(1, updatedRows);

        User updatedUser = userRepository.findById(user.getId()).orElse(null);
        assertNotNull(updatedUser);
        assertEquals("Jane Doe", updatedUser.getName());
    }

    @Test
    void testDeleteByName() {
        // Given
        User user1 = new User();
        user1.setName("John Doe");
        entityManager.persist(user1);

        User user2 = new User();
        user2.setName("John Doe");
        entityManager.persist(user2);

        entityManager.flush();

        // When
        int deletedRows = userRepository.deleteByName("John Doe");

        // Then
        assertEquals(2, deletedRows);

        List<User> users = userRepository.findByName("John Doe");
        assertTrue(users.isEmpty());
    }
}

/*
* Buenas prácticas en las pruebas
Uso de @Transactional:
@DataJpaTest ya habilita el rollback automático después de cada prueba, por lo que no es necesario agregar @Transactional en las pruebas.
Sin embargo, si necesitas controlar manualmente las transacciones, puedes usar @Transactional en los métodos de prueba.

Pruebas independientes:
Cada prueba en UserRepositoryTest es independiente y no depende del estado de otras pruebas.
Esto se logra gracias al rollback automático de @DataJpaTest, que restaura la base de datos a su estado inicial después de cada prueba.

* Evitar dependencias externas:
Se utiliza H2 como base de datos en memoria, lo que garantiza que no haya dependencias externas.
Esto hace que las pruebas sean rápidas y confiables.
*
* */