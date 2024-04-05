package com.paymybuddy.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/transfer", true)
                        .permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/login?logout")
                        .permitAll())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/webjars/**").permitAll()
                        .anyRequest().authenticated())
                .rememberMe(rememberMe -> rememberMe
                        .tokenValiditySeconds(86400)
                        .rememberMeParameter("remember-me"));

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
