package com.paymybuddy.security;

import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Custom UserDetailsService implementation.
 * This class is used to retrieve user-related data. It is used by the Spring Security framework to handle user authentication.
 */
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // UserRepository instance for accessing user data from the database
    private UserRepository userRepository;


    /**
     * Loads the user data which is required by the Spring Security framework to authenticate the user.
     *
     * @param userEmail the email of the user to load
     * @return a UserDetails object representing the user's data
     * @throws UsernameNotFoundException if the user could not be found
     */
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(userEmail);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + userEmail);
        }

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.singleton(authority));
    }
}