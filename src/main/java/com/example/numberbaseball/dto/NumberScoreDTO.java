package com.example.numberbaseball.dto;

import com.example.numberbaseball.domain.User;

import lombok.Getter;

@Getter
public class NumberScoreDTO {

    private String sessionId;
    private String userName;
    private String guessNumber;
    private int str;
    private int ball;
    private int out;

    public NumberScoreDTO(User user, String guessNumber, int[] score) {
        this.sessionId = user.getSessionId();
        this.userName = user.getUserName();
        this.guessNumber = guessNumber;
        this.str = score[0];
        this.ball = score[1];
        this.out = score[2];
    }
}
