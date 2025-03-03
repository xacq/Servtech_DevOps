package dev.magadiflo.app.integrationTest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.magadiflo.app.model.dto.Transaction;
import dev.magadiflo.app.model.entity.Account;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = {"/account-script/test-account-cleanup.sql", "/account-script/test-account-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTestRestTemplateIntegrationTest {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    private String absolutePathOfAccounts;

    @PostConstruct
    public void init() {
        this.absolutePathOfAccounts = "http://localhost:%d/api/v1/accounts".formatted(this.port);
    }

    @Test
    void shouldFindAllAccounts() throws IOException {
        // given
        // when
        ResponseEntity<Account[]> response = this.client.getForEntity(this.absolutePathOfAccounts, Account[].class);
        Account[] accountsDB = response.getBody();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(accountsDB);
        assertEquals(4, accountsDB.length);
        assertEquals(1L, accountsDB[0].getId());
        assertEquals("Andrés", accountsDB[0].getPerson());
        assertEquals(3000D, accountsDB[0].getBalance().doubleValue());

        JsonNode jsonNode = this.objectMapper.readTree(this.objectMapper.writeValueAsBytes(accountsDB));
        assertEquals(3L, jsonNode.get(2).path("id").asLong());
        assertEquals("Liz", jsonNode.get(2).path("person").asText());
        assertEquals(3000D, jsonNode.get(2).path("balance").asDouble());
    }

    @Test
    void shouldFindAnAccount() {
        // given
        Long accountId = 4L;
        Account expectedAccount = new Account(accountId, "Karen", new BigDecimal("3000"));

        // when
        ResponseEntity<Account> response = this.client.getForEntity(this.absolutePathOfAccounts + "/{accountId}", Account.class, accountId);
        Account accountResponse = response.getBody();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(accountResponse);
        assertEquals(expectedAccount.getId(), accountResponse.getId());
        assertEquals(expectedAccount.getPerson(), accountResponse.getPerson());
        assertEquals(expectedAccount.getBalance().doubleValue(), accountResponse.getBalance().doubleValue());
        assertEquals(expectedAccount, accountResponse);
    }

    @Test
    void shouldSaveAnAccount() {
        // given
        Long expectedId = 5L;
        Account accountToSave = new Account(null, "Nophy", new BigDecimal("3000"));

        // when
        ResponseEntity<Account> response = this.client.postForEntity(this.absolutePathOfAccounts, accountToSave, Account.class);
        Account accountResponse = response.getBody();

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(accountResponse);
        assertEquals(expectedId, accountResponse.getId());
        assertEquals(accountToSave.getPerson(), accountResponse.getPerson());
        assertEquals(accountToSave.getBalance().doubleValue(), accountResponse.getBalance().doubleValue());
    }

    @Test
    void shouldTransferAmountBetweenTwoAccounts() throws JsonProcessingException {
        // given
        Transaction dto = new Transaction(1L, 1L, 2L, new BigDecimal("100"));

        // when
        ResponseEntity<String> response = this.client.postForEntity(this.absolutePathOfAccounts + "/transfer", dto, String.class);
        String jsonString = response.getBody();
        JsonNode jsonNode = this.objectMapper.readTree(jsonString);

        // then
        assertNotNull(jsonString);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("Transferencia exitosa", jsonNode.get("message").asText());
    }

    @Test
    void shouldDeletedAnAccountWithExchange() {
        // given
        Long idToDelete = 1L;

        // when
        ResponseEntity<Void> response = this.client.exchange(this.absolutePathOfAccounts + "/{accountId}", HttpMethod.DELETE, null, Void.class, idToDelete);

        // then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(response.hasBody());

        ResponseEntity<Account> responseAccount = this.client.getForEntity(this.absolutePathOfAccounts + "/{accountId}", Account.class, idToDelete);
        assertNull(responseAccount.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseAccount.getStatusCode());
        assertFalse(responseAccount.hasBody());
    }

    @Test
    void shouldDeletedAnAccountWithDelete() {
        // given
        Long idToDelete = 1L;
        ResponseEntity<Account> responseAccount = this.client.getForEntity(this.absolutePathOfAccounts + "/{accountId}", Account.class, idToDelete);
        assertNotNull(responseAccount.getBody());
        assertEquals(HttpStatus.OK, responseAccount.getStatusCode());
        assertTrue(responseAccount.hasBody());

        // when
        this.client.delete("/api/v1/accounts/{accountId}", idToDelete);

        // then
        responseAccount = this.client.getForEntity(this.absolutePathOfAccounts + "/{accountId}", Account.class, idToDelete);
        assertNull(responseAccount.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseAccount.getStatusCode());
        assertFalse(responseAccount.hasBody());
    }
}
