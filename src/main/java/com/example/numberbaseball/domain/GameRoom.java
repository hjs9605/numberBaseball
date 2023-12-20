package com.example.numberbaseball.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class GameRoom {

    private final String roomId;
    private List<Client> clients;

    public GameRoom() {
        this.roomId = UUID.randomUUID().toString().replace("-","").substring(0,8);
        this.clients = new ArrayList<>();
    }

    public void addClient(Client client) {
        this.clients.add(client);
    }
}
