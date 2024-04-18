package com.paymybuddy.repository;

import com.paymybuddy.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing users.
 * This interface provides methods to interact with the database to retrieve and save user data.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    /**
     * Finds a user by their email.
     * @param email The email of the user to find.
     * @return The user with the given email.
     */
    User findUserByEmail(String email);

    /**
     * Finds a user by their ID.
     * @param userId The ID of the user to find.
     * @return The user with the given ID.
     */
    User findUserByUserId(int userId);

    /**
     * Finds users by their email and role.
     * @param userEmail The email of the users to exclude.
     * @param userRole The role of the users to find.
     * @return A list of users with the given role, excluding the user with the given email.
     */
    List<User> findUsersByEmailIsNotAndRole(String userEmail, String userRole);
}