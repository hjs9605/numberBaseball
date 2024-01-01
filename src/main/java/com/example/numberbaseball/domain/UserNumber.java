package com.example.numberbaseball.domain;

import com.example.numberbaseball.vo.BaseBallNumber;
import lombok.Getter;

@Getter
public class UserNumber {

    private String sessionId;
    private BaseBallNumber number;

    public UserNumber(String sessionId, BaseBallNumber number) {
        this.sessionId = sessionId;
        this.number = number;
    }
}
