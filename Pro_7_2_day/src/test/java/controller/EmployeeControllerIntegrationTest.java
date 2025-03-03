package controller;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Usar H2 en pruebas
public class EmployeeControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll(); // Limpiar la base de datos antes de cada test
    }

    @Test
    void whenGetAllEmployees_thenReturnEmptyList() {
        ResponseEntity<List<Employee>> response = restTemplate.exchange(
                "/api/employees",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void whenCreateEmployee_thenReturnCreatedEmployee() {
        Employee employee = new Employee("John Doe", "john@example.com");
        ResponseEntity<Employee> response = restTemplate.postForEntity(
                "/api/employees",
                employee,
                Employee.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    void whenGetEmployeeById_thenReturnEmployee() {
        Employee savedEmployee = repository.save(new Employee("Jane Doe", "jane@example.com"));
        ResponseEntity<Employee> response = restTemplate.getForEntity(
                "/api/employees/" + savedEmployee.getId(),
                Employee.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo(savedEmployee.getName());
    }

    @Test
    void whenDeleteEmployee_thenReturnNoContent() {
        Employee savedEmployee = repository.save(new Employee("Temp", "temp@example.com"));
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/employees/" + savedEmployee.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(repository.existsById(savedEmployee.getId())).isFalse();
    }
}