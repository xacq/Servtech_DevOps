Feature: Prueba de login

  Scenario: Usuario ingresa credenciales correctas
    Given el usuario está en la página de login
    When ingresa "admin" en el campo usuario
    And ingresa "admin123" en el campo contraseña
    And hace clic en el botón de login
    Then debería ver el mensaje "Bienvenido"
