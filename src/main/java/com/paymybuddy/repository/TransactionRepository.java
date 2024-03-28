package com.paymybuddy.repository;

import com.paymybuddy.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    Page<Transaction> findTransactionsBySender_UserId(int sender_userId, Pageable pageable);
}