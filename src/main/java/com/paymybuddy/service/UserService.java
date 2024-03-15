package com.paymybuddy.service;

import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByEmail(String userEmail) {
        return userRepository.findUserByEmail(userEmail);
    }

    public User getUserById(int userId) {
        return userRepository.findUserByUserId(userId);
    }

    public User saveUser(User userToAdd) {
        return userRepository.save(userToAdd);
    }


    public List<User> getOtherUsers(int currentUserId) {
        return userRepository.findUsersByUserIdIsNot(currentUserId);
    }
}