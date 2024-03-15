package com.paymybuddy.repository;

import com.paymybuddy.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findUserByEmail(String email);

    User findUserByUserId(int userId);

    List<User> findUsersByUserIdIsNot(int userId);
}