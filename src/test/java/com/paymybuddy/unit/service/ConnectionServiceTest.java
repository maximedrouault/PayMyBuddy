package com.paymybuddy.unit.service;

import com.paymybuddy.model.Connection;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.ConnectionRepository;
import com.paymybuddy.service.ConnectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConnectionServiceTest {

    @Mock
    private ConnectionRepository connectionRepository;

    @InjectMocks
    private ConnectionService connectionService;


    // getConnections
    @Test
    void getConnections_WhenOwnerEmailIsProvided_ShouldReturnConnectionsList() {
        String ownerEmail = "owner@example.com";
        Connection connection1 = Connection.builder().build();
        Connection connection2 = Connection.builder().build();
        List<Connection> expectedConnections = List.of(connection1, connection2);

        when(connectionRepository.findConnectionsByOwner_EmailAndOwner_Role(ownerEmail, "USER")).thenReturn(expectedConnections);

        List<Connection> foundConnections = connectionService.getConnections(ownerEmail);

        assertEquals(expectedConnections, foundConnections);
        verify(connectionRepository, times(1)).findConnectionsByOwner_EmailAndOwner_Role(ownerEmail, "USER");
    }

    @ParameterizedTest
    @ValueSource(strings = "nonexistent@example.com")
    @NullAndEmptySource
    void getConnections_WhenNoConnectionsExistOrOwnerEmailIsNull_ShouldReturnEmptyList(String ownerEmail) {
        when(connectionRepository.findConnectionsByOwner_EmailAndOwner_Role(ownerEmail, "USER")).thenReturn(List.of());

        List<Connection> foundConnections = connectionService.getConnections(ownerEmail);

        assertTrue(foundConnections.isEmpty());
    }


    // addConnection
    @Test
    void addConnection_WhenUsersAreProvided_ShouldAddConnection() {
        User ownerUser = User.builder().build();
        User receiverUser = User.builder().build();
        Connection connectionToAdd = Connection.builder()
                .owner(ownerUser)
                .receiver(receiverUser)
                .build();

        when(connectionRepository.save(any(Connection.class))).thenReturn(connectionToAdd);

        Connection addedConnection = connectionService.addConnection(ownerUser, receiverUser);

        assertEquals(connectionToAdd, addedConnection);
        verify(connectionRepository, times(1)).save(any(Connection.class));
    }

    @Test
    void addConnection_WhenOwnerUserIsNull_ShouldReturnNull() {
        User receiverUser = User.builder().build();

        Connection addedConnection = connectionService.addConnection(null, receiverUser);

        assertNull(addedConnection);
    }

    @Test
    void addConnection_WhenReceiverUserIsNull_ShouldReturnNull() {
        User ownerUser = User.builder().build();

        Connection addedConnection = connectionService.addConnection(ownerUser, null);

        assertNull(addedConnection);
    }

    @Test
    void addConnection_WhenBothUsersAreNull_ShouldReturnNull() {
        Connection addedConnection = connectionService.addConnection(null, null);

        assertNull(addedConnection);
    }

    @Test
    void addConnection_WhenOwnerUserAndReceiverUserAreSame_ShouldReturnNull() {
        User sameUser = User.builder().build();

        Connection addedConnection = connectionService.addConnection(sameUser, sameUser);

        assertNull(addedConnection);
    }
}