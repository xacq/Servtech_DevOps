Feature: Login en Saucedemo
  Como usuario quiero iniciar sesión correctamente
  Para acceder al sistema

  Scenario: Login exitoso
    Given Estoy en la página de login de Saucedemo
    When Ingreso el usuario "standard_user"
    And Ingreso la contraseña "secret_sauce"
    And Hago clic en el botón de login
    Then Debo ver la página de inventario
    And Debo ver el título "Swag Labs"