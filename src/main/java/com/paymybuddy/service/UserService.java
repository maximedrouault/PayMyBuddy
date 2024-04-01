package com.paymybuddy.service;

import com.paymybuddy.model.Connection;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ConnectionService connectionService;

    public User getUserByEmail(String userEmail) {
        return userRepository.findUserByEmail(userEmail);
    }

    public User getUserById(int userId) {
        return userRepository.findUserByUserId(userId);
    }

    public User saveUser(User userToAdd) {
        return userRepository.save(userToAdd);
    }


    public List<User> getConnectableUsers(String currentUserEmail) {
        List<User> connectableUsers = userRepository.findUsersByEmailIsNot(currentUserEmail);
        List<User> connectedUsers = connectionService.getConnections(currentUserEmail)
                .stream()
                .map(Connection::getReceiver)
                .toList();

        connectableUsers.removeAll(connectedUsers);

        return connectableUsers;
    }
}