package com.example.numberbaseball.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomResponseDTO {

    private String roomCode;

    public RoomResponseDTO(String roomCode) {
        this.roomCode = roomCode;
    }
}
