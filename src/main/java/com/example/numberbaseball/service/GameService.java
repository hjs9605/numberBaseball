package com.example.numberbaseball.service;

import com.example.numberbaseball.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.numberbaseball.Enum.RoomStatus;
import com.example.numberbaseball.domain.Room;
import com.example.numberbaseball.domain.User;
import com.example.numberbaseball.domain.UserNumber;
import com.example.numberbaseball.repository.NumberRepository;
import com.example.numberbaseball.repository.RoomRepository;
import com.example.numberbaseball.repository.UserRepository;
import com.example.numberbaseball.vo.BaseBallNumber;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {

	private final RoomRepository roomRepository;
	private final NumberRepository numberRepository;
	private final UserRepository userRepository;

	private final MessageService messageService;

	@Autowired
	private final SimpMessagingTemplate messagingTemplate;


	public void createOrJoinRoom(String userName, String roomCode, String sessionId){
		if(sessionId == null){
			throw new RuntimeException(userName + "의 session id is null");
		}

		User user = new User(sessionId, userName);
		userRepository.save(user);

		Room room = roomRepository.findByCode(roomCode);
		room.addUser(user);
		RoomStatus roomStatus = room.getRoomStatus();


		if(roomStatus.equals(RoomStatus.WAITING)) {
			messageService.sendWaitMessage(sessionId, roomStatus);
		}
		else {

			User counsterUser = room.getUserList().stream().filter( a -> !a.equals(user)).findFirst().orElseThrow(()-> new IllegalArgumentException("User 없음"));
			String counterSessionId = counsterUser.getSessionId();
			messageService.sendCounterUserEnteredRoomMessage(counterSessionId, userName);

			UserResponseDTO userResponseDTO = new UserResponseDTO(sessionId, userName, roomCode, roomStatus == RoomStatus.READY);
			UserResponseDTO counterUserResponseDTO = new UserResponseDTO(counterSessionId, counsterUser.getUserName(), roomCode, roomStatus == RoomStatus.READY);
			messageService.sendGameStartMessage(sessionId, userResponseDTO);
			messageService.sendGameStartMessage(counterSessionId, counterUserResponseDTO);
		}

	}

	public void setNumber(String sessionId, NumberResponseDTO numberResponseDTO){
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

	public void startGame(String sessionId, StartGameUserDTO startGameUserDTO) {

		User user = userRepository.findBySessionId(sessionId);
		user.setReady();

		Room room = roomRepository.findByCode(startGameUserDTO.getRoomCode());
		User counterUser = room.getCounterUser(user);
		if(room.doAllUsersReady()){
			User startUser = room.getRandomUser();
			messageService.sendYourTurnMessage(startUser);
			User notStartUser = room.getCounterUser(startUser);
			messageService.sendNotYourTurnMessage(notStartUser,startUser);
			return;
		}
		messageService.sendWaitForCounterUserReady(user, counterUser.getUserName());


	}

	public void numberGuess(String sessionId, NumberGuessDTO numberGuessDTO){
		User user = userRepository.findBySessionId(sessionId);
		Room room = roomRepository.findByCode(numberGuessDTO.getRoomCode());
		String roomCode = room.getCode();
		User counterUser = room.getUserList().stream().filter( a -> !a.equals(user)).findFirst().orElseThrow(()-> new IllegalArgumentException("User 없음"));
		BaseBallNumber targetNumber = numberRepository.findBySessionId(counterUser.getSessionId()).getNumber();

		try {

			BaseBallNumber tmpNumber = numberGuessDTO.getNumber();
			int[] score = compare(targetNumber, tmpNumber);

			messageService.sendGuessResultMessage(user,roomCode, tmpNumber.getNumber(), score);
			messageService.sendYourTurnMessage(counterUser);
			messageService.sendNotYourTurnMessage(user, counterUser);
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
