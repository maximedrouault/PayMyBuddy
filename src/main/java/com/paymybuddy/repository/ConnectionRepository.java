package com.paymybuddy.repository;

import com.paymybuddy.model.Connection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepository extends CrudRepository<Connection, Integer> {

    List<Connection> findConnectionsByOwner_Email(String ownerUserEmail);

    Integer deleteConnectionByOwner_UserIdAndReceiver_UserId(int ownerUserId, int receiverUserId);
}