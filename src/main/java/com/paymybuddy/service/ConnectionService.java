package com.paymybuddy.service;

import com.paymybuddy.model.Connection;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing connections between users.
 * This class provides methods to get a list of connections for a user and to add a new connection between two users.
 * It uses the ConnectionRepository to interact with the database.
 */
@Service
@RequiredArgsConstructor
public class ConnectionService {

    // ConnectionRepository instance for accessing connection data from the database
    private final ConnectionRepository connectionRepository;


    /**
     * Retrieves a list of connections for a user.
     *
     * @param ownerUserEmail the email of the user whose connections are to be retrieved
     * @return a list of Connection objects representing the user's connections
     */
    public List<Connection> getConnections(String ownerUserEmail) {
        return connectionRepository.findConnectionsByOwner_EmailAndOwner_Role(ownerUserEmail, "USER");
    }

    /**
     * Adds a new connection between two users.
     *
     * @param ownerUser the user who is adding the connection
     * @param receiverUser the user who is receiving the connection
     * @return the Connection object representing the new connection
     */
    public Connection addConnection(User ownerUser, User receiverUser) {
        Connection connectionToAdd = Connection.builder()
                .owner(ownerUser)
                .receiver(receiverUser)
                .build();

        return connectionRepository.save(connectionToAdd);
    }
}