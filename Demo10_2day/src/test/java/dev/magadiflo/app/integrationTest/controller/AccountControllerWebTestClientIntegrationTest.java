package dev.magadiflo.app.integrationTest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.magadiflo.app.model.dto.Transaction;
import dev.magadiflo.app.model.entity.Account;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Tag(value = "integration_test_webtestclient")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerWebTestClientIntegrationTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void shouldFindAllAccountsWithJsonPath() {
        // given
        // when
        WebTestClient.ResponseSpec response = this.client
                .get()
                .uri("/api/v1/accounts")
                .exchange();

        // then
        response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").value(Matchers.hasSize(2))
                .jsonPath("$.size()").isEqualTo(2)
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].person").isEqualTo("Martín")
                .jsonPath("$[0].balance").isEqualTo(2000)
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].person").isEqualTo("Alicia")
                .jsonPath("$[1].balance").isEqualTo(1000);
    }

    @Test
    @Order(2)
    void shouldFindAnAccount() {
        // given
        Long accountId = 1L;
        Account expectedAccount = new Account(accountId, "Martín", new BigDecimal("2000"));

        // when
        WebTestClient.ResponseSpec response = this.client
                .get()
                .uri("/api/v1/accounts/{accountId}", accountId)
                .exchange();

        // then
        response.expectStatus().isOk()
                .expectBody(Account.class)
                .consumeWith(result -> {
                    Account accountResponse = result.getResponseBody();
                    assertEquals(expectedAccount, accountResponse);
                });
    }

    @Test
    @Order(3)
    void shouldFindAnAccountWithJsonPath() throws JsonProcessingException {
        // given
        Long accountId = 1L;
        Account expectedAccount = new Account(accountId, "Martín", new BigDecimal("2000"));

        // when
        WebTestClient.ResponseSpec response = this.client
                .get()
                .uri("/api/v1/accounts/{accountId}", accountId)
                .exchange();

        // then
        response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.person").isEqualTo(expectedAccount.getPerson())
                .jsonPath("$.balance").isEqualTo(expectedAccount.getBalance().doubleValue())
                .json(this.objectMapper.writeValueAsString(expectedAccount));

    }

    @Test
    @Order(4)
    void shouldSaveAnAccountWithConsumeWith() {
        // given
        Long idDB = 3L;
        Account accountToSave = new Account(null, "Livved", new BigDecimal("3000"));

        // when
        WebTestClient.ResponseSpec response = this.client
                .post()
                .uri("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(accountToSave)
                .exchange();

        // then
        response.expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().location("/api/v1/accounts/" + idDB)
                .expectBody(Account.class)
                .consumeWith(result -> {
                    Account accountDB = result.getResponseBody();

                    assertNotNull(accountDB);
                    assertEquals(idDB, accountDB.getId());
                    assertEquals(accountToSave.getPerson(), accountDB.getPerson());
                    assertEquals(accountToSave.getBalance(), accountDB.getBalance());
                });
    }

    @Test
    @Order(5)
    void shouldSaveAnAccountWithJsonPath() {
        // given
        Long idDB = 4L;
        Account accountToSave = new Account(null, "María", new BigDecimal("5000"));

        // when
        WebTestClient.ResponseSpec response = this.client
                .post()
                .uri("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(accountToSave)
                .exchange();

        // then
        response.expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().location("/api/v1/accounts/" + idDB)
                .expectBody()
                .jsonPath("$.id").isEqualTo(idDB)
                .jsonPath("$.person").value(Matchers.is(accountToSave.getPerson()))
                .jsonPath("$.balance").isEqualTo(accountToSave.getBalance());
    }

    @Test
    @Order(6)
    void shouldTransferAmountBetweenTwoAccounts() {
        // given
        Transaction dto = new Transaction(1L, 1L, 2L, new BigDecimal("100"));

        // when
        WebTestClient.ResponseSpec response = this.client
                .post()
                .uri("/api/v1/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange();

        // then
        response.expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.message").value(Matchers.is("Transferencia exitosa"))
                .jsonPath("$.message").value(message -> assertEquals("Transferencia exitosa", message))
                .jsonPath("$.message").isEqualTo("Transferencia exitosa")
                .jsonPath("$.code").isEqualTo(HttpStatus.CREATED.value())
                .jsonPath("$.transaction.sourceAccountId").isEqualTo(dto.sourceAccountId())
                .jsonPath("$.datetime").value(datetime -> {
                    LocalDateTime localDateTime = LocalDateTime.parse(datetime.toString());
                    assertEquals(LocalDate.now(), localDateTime.toLocalDate());
                });
    }

    @Test
    @Order(7)
    void shouldTransferAmountBetweenTwoAccountsWithConsumeWith() {
        // given
        Transaction dto = new Transaction(1L, 1L, 2L, new BigDecimal("100"));

        // when
        WebTestClient.ResponseSpec response = this.client
                .post()
                .uri("/api/v1/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange();

        // then
        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(result -> {
                    try {
                        JsonNode jsonNode = this.objectMapper.readTree(result.getResponseBody());

                        assertEquals("Transferencia exitosa", jsonNode.path("message").asText());
                        assertEquals(HttpStatus.CREATED.value(), jsonNode.path("code").asInt());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Test
    @Order(8)
    void shouldDeletedAnAccount() {
        // given
        Long idToDelete = 1L;

        // when
        WebTestClient.ResponseSpec response = this.client
                .delete()
                .uri("/api/v1/accounts/{accountId}", idToDelete)
                .exchange();

        // then
        response.expectStatus().isNoContent()
                .expectBody().isEmpty();

        this.client
                .get()
                .uri("/api/v1/accounts/{accountId}", idToDelete)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .consumeWith(result -> {
                    HttpStatusCode status = result.getStatus();

                    assertTrue(status.isError());
                    assertEquals(404, status.value());
                });
    }
}