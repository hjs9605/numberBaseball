package com.example.numberbaseball.dto;

import lombok.Getter;

@Getter
public class Message {
    private String message;

    public Message() {
    }

    public Message(String message) {
        this.message = message;
    }

}