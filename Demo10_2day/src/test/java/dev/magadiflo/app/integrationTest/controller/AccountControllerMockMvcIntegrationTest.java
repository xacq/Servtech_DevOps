package dev.magadiflo.app.integrationTest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.magadiflo.app.model.dto.Transaction;
import dev.magadiflo.app.model.entity.Account;
import jakarta.annotation.PostConstruct;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Sql(scripts = {"/account-script/test-account-cleanup.sql", "/account-script/test-account-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerMockMvcIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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
    void shouldFindAllAccounts() throws Exception {
        // given
        // when
        ResultActions response = this.mockMvc.perform(get(this.absolutePathOfAccounts));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].person").value("Andrés"))
                .andExpect(jsonPath("$[0].balance").value(3000))
                .andExpect(jsonPath("$[1].person").value("Pedro"))
                .andExpect(jsonPath("$[1].balance").value(3000))
                .andExpect(jsonPath("$[2].person").value("Liz"))
                .andExpect(jsonPath("$[2].balance").value(3000))
                .andExpect(jsonPath("$[3].person").value("Karen"))
                .andExpect(jsonPath("$[3].balance").value(3000))
                .andExpect(jsonPath("$.size()", Matchers.is(4)))
                .andExpect(jsonPath("$", Matchers.hasSize(4)));
    }

    @Test
    void shouldSaveAnAccount() throws Exception {
        // given
        Account account = Account.builder()
                .person("Martín")
                .balance(new BigDecimal("3000"))
                .build();

        // when
        ResultActions response = this.mockMvc.perform(post(this.absolutePathOfAccounts)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(account)));

        // then
        response.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", "/api/v1/accounts/5"))
                .andExpect(jsonPath("$.id", Matchers.is(5)))
                .andExpect(jsonPath("$.person", Matchers.is("Martín")))
                .andExpect(jsonPath("$.balance", Matchers.is(3000)));
    }

    @Test
    void shouldFindAnAccount() throws Exception {
        // given
        Long accountId = 1L;

        // when
        ResultActions response = this.mockMvc.perform(get(this.absolutePathOfAccounts + "/{accountId}", accountId));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(accountId))
                .andExpect(jsonPath("$.person").value("Andrés"))
                .andExpect(jsonPath("$.balance").value(3000));
    }

    @Test
    void shouldReturnEmptyWhenAccountDoesNotExist() throws Exception {
        // Given
        Long accountId = 10L;

        // When
        ResultActions response = this.mockMvc.perform(get(this.absolutePathOfAccounts + "/{accountId}", accountId));

        // Then
        response.andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnANotFoundStatusWhenTheAccountDoesNotExist() throws Exception {
        // given
        Long accountId = 100L;

        // when
        ResultActions response = this.mockMvc.perform(get(this.absolutePathOfAccounts + "/{accountId}", accountId));

        // then
        response.andExpect(status().isNotFound());
    }

    @Test
    void shouldTransferAnAmountBetweenAccounts() throws Exception {
        // given
        Transaction dto = new Transaction(1L, 1L, 2L, new BigDecimal("100"));

        // when
        ResultActions response = this.mockMvc.perform(post(this.absolutePathOfAccounts + "/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(dto)));

        // then
        response.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.datetime").exists())
                .andExpect(jsonPath("$.message").value("Transferencia exitosa"))
                .andExpect(jsonPath("$.transaction.sourceAccountId").value(dto.sourceAccountId()));

        String jsonResponse = response.andReturn().getResponse().getContentAsString();
        JsonNode jsonNode = this.objectMapper.readTree(jsonResponse);

        String dateTime = jsonNode.get("datetime").asText();
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime);

        assertEquals(LocalDate.now(), localDateTime.toLocalDate());
    }

    @Test
    void shouldDeletedAnAccountWithDelete() throws Exception {
        // given
        Long idToDelete = 1L;

        // when
        ResultActions responseDelete = this.mockMvc.perform(delete(this.absolutePathOfAccounts + "/{accountId}", idToDelete));

        // then
        responseDelete.andExpect(status().isNoContent());
        ResultActions responseGet = this.mockMvc.perform(get(this.absolutePathOfAccounts + "/{accountId}", idToDelete));
        responseGet.andExpect(status().isNotFound());
    }
}