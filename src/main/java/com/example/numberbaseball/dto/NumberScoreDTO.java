package com.example.numberbaseball.dto;

import lombok.Getter;

@Getter
public class NumberScoreDTO {

    private String sessionId;
    private String userName;
    private String guessNumber;
    private int str;
    private int ball;
    private int out;

    public NumberScoreDTO(String sessionId, String userName, String guessNumber, int[] score) {
        this.sessionId = sessionId;
        this.userName = userName;
        this.guessNumber = guessNumber;
        this.str = score[0];
        this.ball = score[1];
        this.out = score[2];
    }
}
