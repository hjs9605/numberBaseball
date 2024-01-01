package com.example.numberbaseball.controller;

import com.example.numberbaseball.Enum.RoomStatus;
import com.example.numberbaseball.domain.Room;
import com.example.numberbaseball.domain.User;
import com.example.numberbaseball.domain.UserNumber;
import com.example.numberbaseball.dto.*;
import com.example.numberbaseball.repository.NumberRepository;
import com.example.numberbaseball.repository.RoomRepository;
import com.example.numberbaseball.repository.UserRepository;
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

@Controller
@RequiredArgsConstructor
public class GameController {

    private final RoomRepository roomRepository;
    private final NumberRepository numberRepository;
    private final UserRepository userRepository;
    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/enterRoom")
    public void enter(Principal principal, SimpMessageHeaderAccessor headerAccessor, @Payload EnterRoomPayloadDTO enterRoomPayloadDTO){

        String userName = enterRoomPayloadDTO.getUserName();
        String roomCode = enterRoomPayloadDTO.getRoomCode();
        String sessionId = headerAccessor.getSessionId();

        User user = new User(sessionId, userName);
        userRepository.save(user);

        Room room = roomRepository.findByCode(roomCode);
        room.addUser(user);
        RoomStatus roomStatus = room.getRoomStatus();


        if(roomStatus.equals(RoomStatus.WAITING)) {
            messagingTemplate.convertAndSendToUser(sessionId,
                    "/topic/waiting/", roomStatus, createHeaders(sessionId)
            );
        }
        else {
            User counsterUser = room.getUserList().stream().filter( a -> !a.equals(user)).findFirst().orElseThrow(()-> new IllegalArgumentException("User 없음"));
            String counterSessionId = counsterUser.getSessionId();
            UserResponseDTO userResponseDTO = new UserResponseDTO(sessionId, userName, roomCode, roomStatus == RoomStatus.READY ? true : false);
            UserResponseDTO counterUserResponseDTO = new UserResponseDTO(counterSessionId, counsterUser.getUserName(), roomCode, roomStatus == RoomStatus.READY ? true : false);

            messagingTemplate.convertAndSendToUser(sessionId,
                    "/topic/startGame", userResponseDTO, createHeaders(sessionId)
            );

            messagingTemplate.convertAndSendToUser(counterSessionId,
                    "/topic/startGame", counterUserResponseDTO, createHeaders(counterSessionId)
            );
        }
    }

    @MessageMapping("/setNumber")
    public void setNumber(Principal principal, SimpMessageHeaderAccessor headerAccessor, @Payload NumberResponseDTO numberResponseDTO) {
        String sessionId = headerAccessor.getSessionId();
        User user = userRepository.findBySessionId(sessionId);
        String userName = user.getUserName();
        String roomCode = numberResponseDTO.getRoomCode();

        try {

            BaseBallNumber number = numberResponseDTO.getNumber();

            numberRepository.save(new UserNumber(sessionId, number));

            System.out.println(sessionId + number.getNumber());
            messagingTemplate.convertAndSendToUser(sessionId, "/topic/showNumber",
                    new NumberResponseDTO(sessionId, number.getNumber(), roomCode), createHeaders(sessionId));
        } catch (Exception e) {
            messagingTemplate.convertAndSendToUser(
                    sessionId, "/topic/errors",
                    e.getMessage(), createHeaders(sessionId)
            );

            messagingTemplate.convertAndSendToUser(sessionId, "/topic/error/startGame",
                    new UserResponseDTO(sessionId, userName, roomCode, true), createHeaders(sessionId)
            );

        }
    }
    @MessageMapping("/numberGuess")
    public void numberGuess(Principal principal, SimpMessageHeaderAccessor headerAccessor, @Payload NumberGuessDTO numberGuessDTO) {
        String sessionId = headerAccessor.getSessionId();
        User user = userRepository.findBySessionId(sessionId);
        Room room = roomRepository.findByCode(numberGuessDTO.getRoomCode());
        String roomCode = room.getCode();
        User counterUser = room.getUserList().stream().filter( a -> !a.equals(user)).findFirst().orElseThrow(()-> new IllegalArgumentException("User 없음"));
        BaseBallNumber targetNumber = numberRepository.findBySessionId(counterUser.getSessionId()).getNumber();

        try {

            BaseBallNumber tmpNumber = numberGuessDTO.getNumber();
            int[] score = compare(targetNumber, tmpNumber);

            messagingTemplate.convertAndSend(
                    "/topic/score/"+roomCode,
                    new NumberScoreDTO(sessionId, user.getUserName(), tmpNumber.getNumber(), score)
            );

            messagingTemplate.convertAndSendToUser(
                    counterUser.getSessionId(), "/topic/turnNow",
                    new Message(counterUser.getUserName()  + "님의 차례입니다."), createHeaders(counterUser.getSessionId())
            );
            messagingTemplate.convertAndSendToUser(
                    user.getSessionId(), "/topic/turnNotNow",
                    new Message(counterUser.getUserName()  + "님의 차례입니다."), createHeaders(user.getSessionId())
            );

        } catch (Exception e) {
            messagingTemplate.convertAndSendToUser(
                    sessionId, "/topic/errors",
                    e.getMessage(), createHeaders(sessionId)
            );
        }
    }

    private int[] compare(BaseBallNumber myNumber, BaseBallNumber tmpNumber) {
        char[] tmpArray = tmpNumber.toArray();
        char[] myArray = myNumber.toArray();

        //strike, ball, out
        int[] score = {0,0,3};

        for(int i=0 ; i<3; i++) {
            char target = myArray[i];
            for(int j=0; j<3; j++) {
                char tmp = tmpArray[j];
                if(target == tmp && i==j) {
                    score[0] += 1;
                    score[2] -= 1;
                }
                if(target == tmp && i != j) {
                    score[1] += 1;
                    score[2] -= 1;
                }
            }
        }

        return score ;
    }


    private MessageHeaders createHeaders(@Nullable String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        if (sessionId != null) headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

}
