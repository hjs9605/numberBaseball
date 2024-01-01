package com.example.numberbaseball.dto;

import com.example.numberbaseball.vo.BaseBallNumber;
import lombok.Getter;

@Getter
public class NumberResponseDTO {

    public String sessionId;

    public String number;
    public String roomCode;

    public NumberResponseDTO(String sessionId, String number, String roomCode) {
        this.sessionId = sessionId;
        this.number = number;
        this.roomCode = roomCode;
    }


    public BaseBallNumber getNumber() {
        return new BaseBallNumber(number);
    }
}
