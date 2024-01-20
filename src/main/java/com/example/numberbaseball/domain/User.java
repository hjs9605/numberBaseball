package com.example.numberbaseball.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User {

    private String sessionId;
    private String userName;
    private boolean ready;


    public User(String sessionId, String userName) {
        this.sessionId = sessionId;
        this.userName = userName;
        this.ready = false;
    }

    public void setReady() {
        this.ready = true;
    }
}
