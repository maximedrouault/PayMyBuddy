package com.paymybuddy.controller;

import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/user")
    public User getUserByEmail(@RequestParam String userEmail) {
        return userService.getUserByEmail(userEmail);
    }

    @PostMapping("/user")
    public User saveUser(@RequestBody User userToAdd) {
        return userService.saveUser(userToAdd);
    }

    @GetMapping("/users")
    public List<User> getOtherUsers(@RequestParam int currentUserId) {
        return userService.getOtherUsers(currentUserId);
    }
}
