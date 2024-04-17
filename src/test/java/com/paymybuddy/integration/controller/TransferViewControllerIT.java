package com.paymybuddy.integration.controller;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Fail.fail;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TransferViewControllerIT {

    @Autowired
    private MockMvc mockMvc;


    // transferView
    @Test
    void transferView_WhenUnauthenticatedUser_ShouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/transfer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user1test@example.com")
    void transferView_WhenAuthenticatedUser_ShouldReturnTransferView() throws Exception {
        mockMvc.perform(get("/transfer"))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attribute("connections", hasSize(greaterThan(0))))
                .andExpect(model().attribute("connectableUsers", hasSize(greaterThan(0))))
                .andExpect(model().attribute("transactions", instanceOf(Page.class)))
                .andExpect(model().attribute("transactions", hasProperty("totalElements", greaterThan(0L))));
    }


    // addConnection
    @Test
    void addConnection_WhenUnauthenticatedUser_ShouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/connection"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user1test@example.com")
    void addConnection_WhenConnectableUsers_ShouldRedirectToTransferViewWithSuccessMessage() throws Exception {
        mockMvc.perform(post("/connection")
                        .param("receiverUserId", "4")
                        .with(csrf()))
                .andExpect(flash().attributeExists("successMessage"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfer"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "0"})
    @NullAndEmptySource
    @WithMockUser(username = "user1test@example.com")
    void addConnection_WhenWrongReceiverId_ShouldRedirectToTransferViewWithErrorMessage(String receiverUserId) throws Exception {
        mockMvc.perform(post("/connection")
                        .param("receiverUserId", receiverUserId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attributeHasFieldErrors("connectionFormDTO", "receiverUserId"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2"})
    @WithMockUser(username = "user1test@example.com")
    void addConnection_WhenSameOrAlreadyConnectedUser_ShouldThrowDataIntegrityViolationException(String receiverUserId) {
        try {
            mockMvc.perform(post("/connection")
                    .param("receiverUserId", receiverUserId)
                    .with(csrf()));
            fail("Expected DataIntegrityViolationException to be thrown");
        } catch (Exception e) {
            assertInstanceOf(DataIntegrityViolationException.class, e.getCause());
        }
    }

    @Test
    @WithMockUser(username = "user1test@example.com")
    void addConnection_WhenConnectableUserDoesNotExist_ShouldThrowConstraintViolationException() {
        try {
            mockMvc.perform(post("/connection")
                            .param("receiverUserId", "10")
                            .with(csrf()));
            fail("Expected ConstraintViolationException to be thrown");
        } catch (Exception e) {
            assertInstanceOf(ConstraintViolationException.class, e.getCause());
        }
    }


    // addTransaction
    @Test
    void addTransaction_WhenUnauthenticatedUser_ShouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/transaction"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user1test@example.com")
    void addTransaction_WhenValidInformations_ShouldRedirectToTransferViewWithSuccessMessage() throws Exception {
        mockMvc.perform(post("/transaction")
                        .param("receiverUserId", "2")
                        .param("description", "Test transaction")
                        .param("transactionAmount", "20.00")
                        .with(csrf()))
                .andExpect(flash().attributeExists("successMessage"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfer"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "0"})
    @NullAndEmptySource
    @WithMockUser(username = "user1test@example.com")
    void addTransaction_WhenWrongReceiverUserId_ShouldRedirectToTransferViewWithErrorMessage(String receiverUserId) throws Exception {
        mockMvc.perform(post("/transaction")
                        .param("receiverUserId", receiverUserId)
                        .param("description", "Test transaction")
                        .param("transactionAmount", "20.00")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attributeHasFieldErrors("transactionFormDTO", "receiverUserId"));
    }

    @Test
    @WithMockUser(username = "user1test@example.com")
    void addTransaction_WhenSameUser_ShouldThrowDataIntegrityViolationException() {
        try {
            mockMvc.perform(post("/transaction")
                    .param("receiverUserId", "1")
                    .param("description", "Test transaction")
                    .param("transactionAmount", "20.00")
                    .with(csrf()));
            fail("Expected DataIntegrityViolationException to be thrown");
        } catch (Exception e) {
            assertInstanceOf(DataIntegrityViolationException.class, e.getCause());
        }
    }

    @ParameterizedTest
    @NullAndEmptySource
    @WithMockUser(username = "user1test@example.com")
    void addTransaction_WhenNullOrEmptyDescription_ShouldRedirectToTransferViewWithErrorMessage(String description) throws Exception {
        mockMvc.perform(post("/transaction")
                        .param("receiverUserId", "2")
                        .param("description", description)
                        .param("transactionAmount", "20.00")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attributeHasFieldErrors("transactionFormDTO", "description"));
    }

    @Test
    @WithMockUser(username = "user1test@example.com")
    void addTransaction_WhenTooLongDescription_ShouldRedirectToTransferViewWithErrorMessage() throws Exception {
        mockMvc.perform(post("/transaction")
                        .param("receiverUserId", "2")
                        .param("description", "p".repeat(101))
                        .param("transactionAmount", "20.00")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attributeHasFieldErrors("transactionFormDTO", "description"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-20.00", "0", "100000000.00", "100.0123"})
    @NullAndEmptySource
    @WithMockUser(username = "user1test@example.com")
    void addTransaction_WhenWrongTransactionAmount_ShouldRedirectToTransferViewWithErrorMessage(String transactionAmount) throws Exception {
        mockMvc.perform(post("/transaction")
                        .param("receiverUserId", "2")
                        .param("description", "Test transaction")
                        .param("transactionAmount", transactionAmount)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attributeHasFieldErrors("transactionFormDTO", "transactionAmount"));
    }

    @Test
    @WithMockUser(username = "user1test@example.com")
    void addTransaction_WhenWalletBalanceIsNotSufficientForTransactionAmount_ShouldRedirectToTransferViewWithErrorMessage() throws Exception {
        mockMvc.perform(post("/transaction")
                        .param("receiverUserId", "2")
                        .param("description", "Test transaction")
                        .param("transactionAmount", "1500") // Wallet balance is 1200.00 in the test data
                        .with(csrf()))
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfer"));
    }
}