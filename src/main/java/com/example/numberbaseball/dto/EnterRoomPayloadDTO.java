package com.example.numberbaseball.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EnterRoomPayloadDTO {

    private String userName;
    private String roomCode;

    public EnterRoomPayloadDTO(String userName, String roomCode) {
        this.userName = userName;
        this.roomCode = roomCode;
    }
}
