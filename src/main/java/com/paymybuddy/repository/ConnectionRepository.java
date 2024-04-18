package com.paymybuddy.repository;

import com.paymybuddy.model.Connection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing connections between users.
 * This interface provides methods to interact with the database to retrieve and save connection data.
 */
@Repository
public interface ConnectionRepository extends CrudRepository<Connection, Integer> {

    /**
     * Retrieves a list of connections for a user.
     *
     * @param ownerUserEmail the email of the user whose connections are to be retrieved
     * @param ownerUserRole the role of the user whose connections are to be retrieved
     * @return a list of Connection objects representing the user's connections
     */
    List<Connection> findConnectionsByOwner_EmailAndOwner_Role(String ownerUserEmail, String ownerUserRole);
}