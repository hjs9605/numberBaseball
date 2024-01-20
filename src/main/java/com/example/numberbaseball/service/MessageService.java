package com.example.numberbaseball.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.numberbaseball.Enum.RoomStatus;
import com.example.numberbaseball.dto.Message;
import com.example.numberbaseball.dto.UserResponseDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MessageService {
	@Autowired
	private final SimpMessagingTemplate messagingTemplate;


	public void sendWaitMessage(String receiverSessionId, RoomStatus roomStatus){
		messagingTemplate.convertAndSendToUser(receiverSessionId,
			"/topic/waiting/", roomStatus, createHeaders(receiverSessionId)
		);
	}

	public void sendGameStartMessage(String receiverSessionId, UserResponseDTO userResponseDTO){
		messagingTemplate.convertAndSendToUser(receiverSessionId,
			"/topic/startGame", userResponseDTO, createHeaders(receiverSessionId)
		);
	}

	private MessageHeaders createHeaders(@Nullable String sessionId) {
		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
		if (sessionId != null) headerAccessor.setSessionId(sessionId);
		headerAccessor.setLeaveMutable(true);
		return headerAccessor.getMessageHeaders();
	}

	public void sendCounterUserEnteredRoomMessage(String receiverSessionId, String incomingUserName) {
		messagingTemplate.convertAndSendToUser(receiverSessionId, "/topic/greeting", new Message(incomingUserName + "이 입장하셨습니다."), createHeaders(receiverSessionId));
	}
}
