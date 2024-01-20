package com.example.numberbaseball.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.example.numberbaseball.dto.EnterRoomPayloadDTO;
import com.example.numberbaseball.dto.NumberGuessDTO;
import com.example.numberbaseball.dto.NumberResponseDTO;
import com.example.numberbaseball.service.GameService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;


    @MessageMapping("/enterRoom")
    public void enter(Principal principal, SimpMessageHeaderAccessor headerAccessor, @Payload EnterRoomPayloadDTO enterRoomPayloadDTO){
        String userName = enterRoomPayloadDTO.getUserName();
        String roomCode = enterRoomPayloadDTO.getRoomCode();
        String sessionId = headerAccessor.getSessionId();
        gameService.createOrJoinRoom(userName, roomCode, sessionId);
    }

    @MessageMapping("/setNumber")
    public void setNumber(Principal principal, SimpMessageHeaderAccessor headerAccessor, @Payload NumberResponseDTO numberResponseDTO) {
        String sessionId = headerAccessor.getSessionId();
        gameService.setNumber(sessionId, numberResponseDTO);


    }
    @MessageMapping("/numberGuess")
    public void numberGuess(Principal principal, SimpMessageHeaderAccessor headerAccessor, @Payload NumberGuessDTO numberGuessDTO) {
        String sessionId = headerAccessor.getSessionId();
        gameService.numberGuess(sessionId, numberGuessDTO);
    }





}
