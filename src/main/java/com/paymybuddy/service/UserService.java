package com.paymybuddy.service;

import com.paymybuddy.model.Connection;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.ConnectionRepository;
import com.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing users.
 * This class provides methods to get a user by email or ID, update a user, and get a list of connectable users for a user.
 * It uses the UserRepository and ConnectionRepository to interact with the database.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    // UserRepository instance for accessing user data from the database
    private final UserRepository userRepository;
    // ConnectionRepository instance for accessing connection data from the database
    private final ConnectionRepository connectionRepository;


    /**
     * Retrieves a user by email.
     *
     * @param userEmail the email of the user to retrieve
     * @return the User object representing the user
     */
    public User getUserByEmail(String userEmail) {
        return userRepository.findUserByEmail(userEmail);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the User object representing the user
     */
    public User getUserById(int userId) {
        return userRepository.findUserByUserId(userId);
    }

    /**
     * Updates a user.
     *
     * @param userToUpdate the User object representing the user to update
     * @return the User object representing the updated user
     */
    public User updateUser(User userToUpdate) {
        return userRepository.save(userToUpdate);
    }

    /**
     * Retrieves a list of users who can be connected to a user.
     *
     * @param currentUserEmail the email of the user for whom to retrieve connectable users
     * @return a list of User objects representing the connectable users
     */
    public List<User> getConnectableUsers(String currentUserEmail) {
        List<User> connectableUsers = userRepository.findUsersByEmailIsNotAndRole(currentUserEmail, "USER");
        List<Connection> connectionsOfCurrentUser = connectionRepository.findConnectionsByOwner_EmailAndOwner_Role(currentUserEmail, "USER");

        connectableUsers.removeAll(connectionsOfCurrentUser.stream().map(Connection::getReceiver).toList());

        return connectableUsers;
    }
}