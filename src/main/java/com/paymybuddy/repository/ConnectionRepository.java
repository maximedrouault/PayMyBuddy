package com.paymybuddy.repository;

import com.paymybuddy.model.Connection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepository extends CrudRepository<Connection, Integer> {

    List<Connection> findConnectionsByOwner_EmailAndOwner_Role(String ownerUserEmail, String ownerUserRole);
}