package com.paymybuddy.integration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransactionControllerIT {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void getCommissions_WhenUnauthenticatedUser_ShouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/commissions"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    // Test if secure endpoint isn't accessible for not allowed user, based on role
    @Test
    @WithMockUser(username = "user1test@example.com") // With roles = {"USER"}
    void getCommissions_WhenUserWithRoleUser_ShouldNotHaveAccessToCommission() throws Exception {
        mockMvc.perform(get("/commissions"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admintest@example.com", roles = "ADMIN")
    void getCommissions_WhenUserWithRoleAdmin_ShouldHaveAccessToCommission() throws Exception {
        mockMvc.perform(get("/commissions"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "otherrole@example.com", roles = "OTHERROLE")
    void getCommissions_WhenAuthenticatedUserWithOtherRole_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/commissions"))
                .andExpect(status().isForbidden());
    }


    // getCommissions
    @Test
    @WithMockUser(username = "admintest@example.com", roles = "ADMIN")
    void getCommissions_WhenAuthenticatedUserWithRoleAdmin_ShouldReturnCommissionsList() throws Exception {
        mockMvc.perform(get("/commissions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(19))
                .andExpect(jsonPath("$[0].transactionId").value(1))
                .andExpect(jsonPath("$[0].date").value("2023-03-25"))
                .andExpect(jsonPath("$[0].time").value("10:00:00"))
                .andExpect(jsonPath("$[0].commissionAmount").value(5.01));
    }

    @Test
    @WithMockUser(username = "user1test@example.com", roles = "USER")
    void getCommissions_WhenAuthenticatedUserWithRoleUser_ShouldNotReturnCommissionsList() throws Exception {
        mockMvc.perform(get("/commissions"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").doesNotExist());
    }
}