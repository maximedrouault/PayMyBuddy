package com.paymybuddy.integration.controller;

import com.paymybuddy.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ProfileViewControllerIT {

    @Autowired
    private MockMvc mockMvc;


    // profileView
    @Test
    void profileView_WhenUnauthenticatedUser_ShouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user1test@example.com")
    void profileView_WhenAuthenticatedUser_ShouldReturnProfileView() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("currentUser", instanceOf(User.class)));
    }


    // addMoneyToWallet
    @Test
    void addMoneyToWallet_WhenUnauthenticatedUser_ShouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/profile/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user1test@example.com")
    void addMoneyToWallet_WhenAmountToAddIsValid_ShouldRedirectToProfileViewWithSuccessMessage() throws Exception {
        mockMvc.perform(post("/profile/add")
                        .param("amountToAdd", "20.00")
                        .with(csrf()))
                .andExpect(flash().attributeExists("successAddMessage"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-20.00", "0.00", "100000000.00", "100.0123"})
    @NullAndEmptySource
    @WithMockUser(username = "user1test@example.com")
    void addMoneyToWallet_WhenAmountToAddIsInvalid_ShouldRedirectToProfileViewWithErrorMessage(String amountToAdd) throws Exception {
        mockMvc.perform(post("/profile/add")
                        .param("amountToAdd", amountToAdd)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeHasFieldErrors("walletAddFormDTO", "amountToAdd"));
    }

    @Test
    @WithMockUser(username = "user1test@example.com")
    void addMoneyToWallet_WhenAmountToAddExceedsMaxBalance_ShouldRedirectToProfileViewWithErrorMessage() throws Exception {
        mockMvc.perform(post("/profile/add")
                        .param("amountToAdd", "99998800.00") // Wallet balance is 1200.00 in the test data
                        .with(csrf()))
                .andExpect(flash().attributeExists("errorAddMessage"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }

    @Test
    @WithMockUser(username = "user1test@example.com")
    void withdrawMoneyFromWallet_WhenAmountToWithdrawIsValid_ShouldRedirectToProfileViewWithSuccessMessage() throws Exception {
        mockMvc.perform(post("/profile/withdraw")
                        .param("amountToWithdraw", "20.00")
                        .with(csrf()))
                .andExpect(flash().attributeExists("successWithdrawMessage"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-20.00", "0.00", "100000000.00", "100.0123"})
    @NullAndEmptySource
    @WithMockUser(username = "user1test@example.com")
    void withdrawMoneyFromWallet_WhenAmountToWithdrawIsInvalid_ShouldRedirectToProfileViewWithErrorMessage(String amountToWithdraw) throws Exception {
        mockMvc.perform(post("/profile/withdraw")
                        .param("amountToWithdraw", amountToWithdraw)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeHasFieldErrors("walletWithdrawFormDTO", "amountToWithdraw"));
    }

    @Test
    @WithMockUser(username = "user1test@example.com")
    void withdrawMoneyFromWallet_WhenAmountToWithdrawExceedsMinBalance_ShouldRedirectToProfileViewWithErrorMessage() throws Exception {
        mockMvc.perform(post("/profile/withdraw")
                        .param("amountToWithdraw", "1300.00") // Wallet balance is 1200.00 in the test data
                        .with(csrf()))
                .andExpect(flash().attributeExists("errorWithdrawMessage"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }
}
