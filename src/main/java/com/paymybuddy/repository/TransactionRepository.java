package com.paymybuddy.repository;

import com.paymybuddy.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing transactions between users.
 * This interface provides methods to interact with the database to retrieve and save transaction data.
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    /**
     * Retrieves a paginated list of transactions for a user.
     *
     * @param senderUserEmail the email of the user whose transactions are to be retrieved
     * @param pageable the Pageable object containing pagination information
     * @return a Page object containing Transaction objects representing the user's transactions
     */
    Page<Transaction> findTransactionsBySender_EmailOrderByDateDescTimeDesc(String senderUserEmail, Pageable pageable);
}