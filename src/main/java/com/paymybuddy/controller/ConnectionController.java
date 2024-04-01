package com.paymybuddy.controller;

import com.paymybuddy.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConnectionController {

    private final ConnectionService connectionService;


//    @GetMapping("/connections")
//    public List<Connection> getConnections(@RequestParam int ownerUserId) {
//        return connectionService.getConnections(ownerUserId);
//    }

//    @PostMapping("/connection")
//    public Connection addConnection(@RequestParam int ownerUserId, int receiverUserId) {
//        User ownerUser = userService.getUserById(ownerUserId);
//        User receiverUser = userService.getUserById(receiverUserId);
//
//        return connectionService.addConnection(ownerUser, receiverUser);
//    }

    @DeleteMapping("/connection")
    public Integer deleteConnection(@RequestParam int currentUserEmail, int receiverUserId) {
        return connectionService.deleteConnection(currentUserEmail, receiverUserId);
    }
}
