package com.paymybuddy.unit.service;

import com.paymybuddy.model.Connection;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.ConnectionRepository;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConnectionRepository connectionRepository;

    @InjectMocks
    private UserService userService;


    // getUserByEmail
    @Test
    void getUserByEmail_WhenUserEmailIsProvided_ShouldReturnFoundUser() {
        String userEmail = "user@example.com";
        User expectedUser = User.builder().build();

        when(userRepository.findUserByEmail(userEmail)).thenReturn(expectedUser);

        User foundUser = userService.getUserByEmail(userEmail);

        assertEquals(foundUser, expectedUser);
        verify(userRepository, times(1)).findUserByEmail(userEmail);
    }

    @ParameterizedTest
    @ValueSource(strings = {"nonexistent@example.com"})
    @NullAndEmptySource
    void getUserByEmail_WhenUserDoesNotExistOrNull_ShouldReturnNull(String userEmail) {
        when(userRepository.findUserByEmail(userEmail)).thenReturn(null);

        User foundUser = userService.getUserByEmail(userEmail);

        assertNull(foundUser);
    }


    // getUserById
    @Test
    void getUserById_WhenUserIdIsProvided_ShouldReturnFoundUser() {
        int userId = 1;
        User expectedUser = User.builder().build();

        when(userRepository.findUserByUserId(userId)).thenReturn(expectedUser);

        User foundUser = userService.getUserById(userId);

        assertEquals(foundUser, expectedUser);
        verify(userRepository, times(1)).findUserByUserId(userId);
    }

    @Test
    void getUserById_WhenUserDoesNotExistOrNull_ShouldReturnNull() {
        int userId = 0;

        when(userRepository.findUserByUserId(userId)).thenReturn(null);

        User foundUser = userService.getUserById(userId);

        assertNull(foundUser);
    }


    // updateUser
    @Test
    void updateUser_WhenUserToUpdateExists_ShouldReturnUpdatedUser() {
        User userToUpdate = User.builder().build();

        when(userRepository.save(any(User.class))).thenReturn(userToUpdate);

        User updatedUser = userService.updateUser(userToUpdate);

        assertEquals(updatedUser, userToUpdate);
        verify(userRepository, times(1)).save(userToUpdate);
    }

    @Test
    void updateUser_WhenUserToUpdateIsNull_ShouldReturnNull() {
        User updatedUser = userService.updateUser(null);

        assertNull(updatedUser);
        verify(userRepository, times(0)).save(any(User.class));
    }


    // getConnectableUsers
    @Test
    void getConnectableUsers_WhenCurrentUserHasConnections_ShouldReturnConnectableUsers() {
        User user1 = User.builder().userId(1).email("user1@example.com").build(); // Current user
        User user2 = User.builder().userId(2).email("user2@example.com").build();
        User user3 = User.builder().userId(3).email("user3@example.com").build();
        String currentUserEmail = user1.getEmail();
        // Other Users are User2 and User3
        List<User> otherUsers = new ArrayList<>(Arrays.asList(user2, user3));
        // User1 is connected with User2
        List<Connection> connectionsOfCurrentUser = List.of(Connection.builder().owner(user1).receiver(user2).build());

        when(userRepository.findUsersByEmailIsNot(currentUserEmail)).thenReturn(otherUsers);
        when(connectionRepository.findConnectionsByOwner_Email(currentUserEmail)).thenReturn(connectionsOfCurrentUser);

        // Connectable user is just User3
        List<User> connectableUsers = userService.getConnectableUsers(currentUserEmail);

        assertEquals(1, connectableUsers.size());
        assertTrue(connectableUsers.contains(user3));
    }

    @Test
    void getConnectableUsers_WhenUserHasNoConnections_ShouldReturnAllOtherUsers() {
        User user1 = User.builder().userId(1).email("user1@example.com").build(); // Current user
        User user2 = User.builder().userId(2).email("user2@example.com").build();
        User user3 = User.builder().userId(3).email("user3@example.com").build();
        String currentUserEmail = user1.getEmail();
        // Other Users are User2 and User3
        List<User> otherUsers = new ArrayList<>(Arrays.asList(user2, user3));
        // User1 is not connected with other users
        List<Connection> connectionsOfCurrentUser = List.of();

        when(userRepository.findUsersByEmailIsNot(currentUserEmail)).thenReturn(otherUsers);
        when(connectionRepository.findConnectionsByOwner_Email(currentUserEmail)).thenReturn(connectionsOfCurrentUser);

        List<User> connectableUsers = userService.getConnectableUsers(currentUserEmail);

        assertEquals(2, connectableUsers.size());
        assertTrue(connectableUsers.contains(user2));
        assertTrue(connectableUsers.contains(user3));
    }

    @Test
    void getConnectableUsers_WhenThereAreNoOtherUsers_ShouldReturnEmptyList() {
        User user1 = User.builder().userId(1).email("user1@example.com").build(); // Current user
        String currentUserEmail = user1.getEmail();

        when(userRepository.findUsersByEmailIsNot(currentUserEmail)).thenReturn(Collections.emptyList());

        List<User> connectableUsers = userService.getConnectableUsers(currentUserEmail);

        assertTrue(connectableUsers.isEmpty());
    }
}
