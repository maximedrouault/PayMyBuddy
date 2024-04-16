package com.paymybuddy.integration.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoginViewControllerIT {

    @Autowired
    private MockMvc mockMvc;


    // loginView
    @Test
    void loginView_WhenAccessingLogin_ShouldReturnLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void loginView_WhenCorrectCredentialsProvided_ShouldAuthenticateUser() throws Exception {
        mockMvc.perform(formLogin("/login").user("user1test@example.com").password("1234")).andExpect(authenticated());
    }

    @ParameterizedTest
    @CsvSource({
            "wronguseremail@example.com, 1234",
            "user1test@example.com, wrongpassword",
            ", 1234",
            "user1test@example.com, ",
            "null, 1234",
            "user1test@example.com, null"
    })
    void loginView_WhenInvalidCredentialsProvided_ShouldNotAuthenticateUser(String email, String password) throws Exception {
        mockMvc.perform(formLogin("/login").user(email).password(password))
                .andExpect(unauthenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void loginView_WhenTooLongCredentialsProvided_ShouldNotAuthenticateUser() throws Exception {
        mockMvc.perform(formLogin("/login").user("l".repeat(101)).password("p".repeat(101)))
                .andExpect(unauthenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    @WithMockUser
    void loginView_WhenAlreadyAuthenticatedUserAccessesTransfer_ShouldReturnTransferPage() throws Exception {
        mockMvc.perform(get("/transfer"))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/transfer", "/unknownurl", "/commissions"})
    void loginView_WhenUnauthenticatedUserAccessesUnknownOrUnauthorizedUrl_ShouldReturnLoginPage(String urlRequest) throws Exception {
        mockMvc.perform(get(urlRequest))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void loginView_WhenCorrectCredentialsProvidedForUser_ShouldAuthenticateUserWithUserRole() throws Exception {
        mockMvc.perform(formLogin("/login").user("user1test@example.com").password("1234"))
                .andExpect(authenticated().withRoles("USER"));
    }

    @Test
    void loginView_WhenCorrectCredentialsProvidedForAdmin_ShouldAuthenticateUserWithAdminRole() throws Exception {
        mockMvc.perform(formLogin("/login").user("admintest@example.com").password("1234"))
                .andExpect(authenticated().withRoles("ADMIN"));
    }
}