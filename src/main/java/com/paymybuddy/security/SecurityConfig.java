package com.paymybuddy.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Configuration for the application.
 * This class is responsible for configuring the security aspects of the application.
 * It defines the security filter chain and the password encoder bean.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Defines the security filter chain for the application.
     * This method configures the security aspects such as URL access restrictions, form login, logout and remember me functionality.
     *
     * @param httpSecurity the HttpSecurity to modify
     * @return the SecurityFilterChain
     * @throws Exception if an error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/webjars/bootstrap/**").permitAll()
                        .requestMatchers("/webjars/bootstrap-icons/**").permitAll()
                        .requestMatchers("/commissions").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/transfer", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())
                .rememberMe(rememberMe -> rememberMe
                        .tokenValiditySeconds(86400)
                        .rememberMeParameter("remember-me"));

        return httpSecurity.build();
    }

    /**
     * Defines the password encoder for the application.
     * This method returns a BCryptPasswordEncoder which is a password encoder that uses the BCrypt strong hashing function.
     *
     * @return the PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
