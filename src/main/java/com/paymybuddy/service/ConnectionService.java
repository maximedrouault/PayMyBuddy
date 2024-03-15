package com.paymybuddy.service;

import com.paymybuddy.model.Connection;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionService {

    private final ConnectionRepository connectionRepository;


    public List<Connection> getConnections(int ownerUserId) {
        return connectionRepository.findConnectionsByOwner_UserId(ownerUserId);
    }

    public Connection addConnection(User ownerUser, User receiverUser) {
        Connection connectionToAdd = Connection.builder()
                .owner(ownerUser)
                .receiver(receiverUser)
                .build();

        return connectionRepository.save(connectionToAdd);
    }

    @Transactional
    public Integer deleteConnection(int ownerUserId, int receiverUserId) {
        return connectionRepository.deleteConnectionByOwner_UserIdAndReceiver_UserId(ownerUserId, receiverUserId);
    }
}
