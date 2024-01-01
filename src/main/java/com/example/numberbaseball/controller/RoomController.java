package com.example.numberbaseball.controller;

import com.example.numberbaseball.domain.Client;
import com.example.numberbaseball.domain.Room;
import com.example.numberbaseball.dto.RoomResponseDTO;
import com.example.numberbaseball.exception.RoomFullException;
import com.example.numberbaseball.exception.RoomNotFoundException;
import com.example.numberbaseball.repository.RoomRepository;
import com.example.numberbaseball.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomRepository roomRepository;
    @GetMapping("/createRoom")
    @ResponseBody
    public RoomResponseDTO createRoom() {
        Room room = roomService.createRoom();
        roomRepository.save(room);
        return new RoomResponseDTO(room.getCode());
    }

    @GetMapping("/joinRoom/{roomCode}")
    @ResponseBody
    public RoomResponseDTO joinRoom(@PathVariable String roomCode) {
        Room room = roomRepository.findByCode(roomCode);

        if (room != null) {
            if(room.getClientList().size()!=1) {
                throw new RoomFullException();
            }
            roomService.joinRoom(room);
        }
    return new RoomResponseDTO(roomCode);
    }


}