package com.example.numberbaseball.dto;

import com.example.numberbaseball.vo.BaseBallNumber;
import lombok.Getter;

@Getter
public class NumberGuessDTO {

    private String sessionId;
    private String roomCode;
    private String number;

    public NumberGuessDTO(String sessionId, String roomCode, String number) {
        this.sessionId = sessionId;
        this.roomCode = roomCode;
        this.number = number;
    }

    public BaseBallNumber getNumber() {
        return new BaseBallNumber(number);
    }
}
