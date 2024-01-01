package com.example.numberbaseball.domain;

import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

@Getter
public class Client {

    private String id;
    public Client() {
        this.id = UUID.randomUUID().toString();
    }
}
