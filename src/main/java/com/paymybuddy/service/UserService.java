package com.paymybuddy.service;

import com.paymybuddy.model.Connection;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.ConnectionRepository;
import com.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ConnectionRepository connectionRepository;


    public User getUserByEmail(String userEmail) {
        return userRepository.findUserByEmail(userEmail);
    }

    public User getUserById(int userId) {
        return userRepository.findUserByUserId(userId);
    }

    public User updateUser(User userToUpdate) {
        return userRepository.save(userToUpdate);
    }

    public List<User> getConnectableUsers(String currentUserEmail) {
        List<User> connectableUsers = userRepository.findUsersByEmailIsNotAndRole(currentUserEmail, "USER");
        List<Connection> connectionsOfCurrentUser = connectionRepository.findConnectionsByOwner_EmailAndOwner_Role(currentUserEmail, "USER");

        connectableUsers.removeAll(connectionsOfCurrentUser.stream().map(Connection::getReceiver).toList());

        return connectableUsers;
    }
}