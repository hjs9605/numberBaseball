package com.example.numberbaseball.domain;

import com.example.numberbaseball.vo.BaseBallNumber;
import lombok.Getter;

@Getter
public class User {

    private String sessionId;
    private String userName;


    public User(String sessionId, String userName) {
        this.sessionId = sessionId;
        this.userName = userName;
    }
}
