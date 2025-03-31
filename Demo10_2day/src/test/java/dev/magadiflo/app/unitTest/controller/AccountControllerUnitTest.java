package dev.magadiflo.app.unitTest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.magadiflo.app.controller.AccountController;
import dev.magadiflo.app.model.dto.Transaction;
import dev.magadiflo.app.model.entity.Account;
import dev.magadiflo.app.service.AccountService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(AccountController.class)
class AccountControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @Test
    void shouldFindAllAccounts() throws Exception {
        // given
        List<Account> accounts = List.of(
                new Account(1L, "Martín", new BigDecimal("2000")),
                new Account(2L, "Alicia", new BigDecimal("1000"))
        );
        when(this.accountService.findAll()).thenReturn(accounts);

        // when
        ResultActions response = this.mockMvc.perform(get("/api/v1/accounts"));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].person").value("Martín"))
                .andExpect(jsonPath("$[0].balance").value(2000))
                .andExpect(jsonPath("$[1].person").value("Alicia"))
                .andExpect(jsonPath("$[1].balance").value(1000))
                .andExpect(jsonPath("$.size()", Matchers.is(accounts.size())))
                .andExpect(jsonPath("$", Matchers.hasSize(accounts.size())))
                .andExpect(content().json(this.objectMapper.writeValueAsString(accounts)));
    }

    @Test
    void shouldSaveAnAccount() throws Exception {
        // given
        Account account = Account.builder()
                .person("Martín")
                .balance(new BigDecimal("2000"))
                .build();
        when(this.accountService.save(account)).then(invocation -> {
            Account accountResponse = invocation.getArgument(0);
            accountResponse.setId(1L);
            return accountResponse;
        });

        // when
        ResultActions response = this.mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(account)));

        // then
        response.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", "/api/v1/accounts/1"))
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.person", Matchers.is("Martín")))
                .andExpect(jsonPath("$.balance", Matchers.is(2000)));
    }

    @Test
    void shouldFindAnAccount() throws Exception {
        // given
        Long accountId = 1L;
        Account account = new Account(1L, "Martín", new BigDecimal("2000"));
        when(this.accountService.findById(accountId)).thenReturn(Optional.of(account));

        // when
        ResultActions response = this.mockMvc.perform(get("/api/v1/accounts/{accountId}", accountId));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person").value("Martín"))
                .andExpect(jsonPath("$.balance").value(2000));
        verify(this.accountService).findById(accountId);
        verify(this.accountService).findById(anyLong());
    }

    @Test
    void shouldReturnANotFoundStatusWhenTheAccountDoesNotExist() throws Exception {
        // given
        Long accountId = 100L;
        when(this.accountService.findById(accountId)).thenReturn(Optional.empty());

        // when
        ResultActions response = this.mockMvc.perform(get("/api/v1/accounts/{accountId}", accountId));

        // then
        response.andExpect(status().isNotFound());
        verify(this.accountService).findById(accountId);
        verify(this.accountService).findById(anyLong());
    }

    @Test
    void shouldTransferAnAmountBetweenAccounts() throws Exception {
        // given
        Transaction dto = new Transaction(1L, 1L, 2L, new BigDecimal("100"));
        doNothing().when(this.accountService).transfer(dto.bankId(), dto.sourceAccountId(), dto.targetAccountId(), dto.amount());

        // when
        ResultActions response = this.mockMvc.perform(post("/api/v1/accounts/transfer")
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
}