package com.example.numberbaseball.controller;

import com.example.numberbaseball.Enum.RoomStatus;
import com.example.numberbaseball.domain.Room;
import com.example.numberbaseball.domain.User;
import com.example.numberbaseball.domain.UserNumber;
import com.example.numberbaseball.dto.*;
import com.example.numberbaseball.repository.NumberRepository;
import com.example.numberbaseball.repository.RoomRepository;
import com.example.numberbaseball.repository.UserRepository;
import com.example.numberbaseball.service.GameService;
import com.example.numberbaseball.vo.BaseBallNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

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

    @MessageMapping("/readyAll")
    public void startGame(Principal principal, SimpMessageHeaderAccessor headerAccessor, @Payload StartGameUserDTO startGameUserDTO) {
        String sessionId = headerAccessor.getSessionId();
        gameService.startGame(sessionId, startGameUserDTO);

    }

    @MessageMapping("/numberGuess")
    public void numberGuess(Principal principal, SimpMessageHeaderAccessor headerAccessor, @Payload NumberGuessDTO numberGuessDTO) {
        String sessionId = headerAccessor.getSessionId();
        gameService.numberGuess(sessionId, numberGuessDTO);
    }





}
