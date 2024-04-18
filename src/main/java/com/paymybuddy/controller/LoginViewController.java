package com.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the login view.
 */
@Controller
public class LoginViewController {

    /**
     * Handles GET requests to the /login endpoint.
     *
     * @return the login view
     */
    @GetMapping("/login")
    public String loginView() {
        return "login";
    }
}