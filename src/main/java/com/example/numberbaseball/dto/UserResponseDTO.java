package com.example.numberbaseball.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDTO {

    private String sessionId;
    private String userName;
    private String roomCode;
    private boolean roomStatus;

    public UserResponseDTO(String sessionId, String userName, String roomCode, boolean roomStatus) {
        this.sessionId = sessionId;
        this.userName = userName;
        this.roomCode = roomCode;
        this.roomStatus = roomStatus;
    }
}
