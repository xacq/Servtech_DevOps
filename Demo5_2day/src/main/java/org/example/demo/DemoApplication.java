package org.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

/*
* Objetivos Cumplidos en el Proyecto

    Orientado a aplicaciones no reactivas:
    Se utiliza spring-boot-starter-web para crear servicios REST tradicionales.

    TestRestTemplate:
    Permite realizar peticiones HTTP reales al servidor embebido.

    Configuración en @SpringBootTest:
    La anotación @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) levanta el servidor en un puerto aleatorio, garantizando pruebas end-to-end.

    Verificación end-to-end:
    Los tests validan tanto la respuesta HTTP (estado, contenido JSON) como el funcionamiento completo del endpoint, desde el controlador hasta la lógica de negocio.
* */