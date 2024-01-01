package com.example.numberbaseball.service;

import com.example.numberbaseball.Enum.RoomStatus;
import com.example.numberbaseball.domain.Client;
import com.example.numberbaseball.domain.Room;
import org.springframework.stereotype.Service;
@Service
public class RoomService {

    public Room createRoom() {
        Room room = new Room(RoomStatus.WAITING);
        room.addClient(new Client());
        return room;
    }

    public Room joinRoom(Room room) {
        room.setRoomStatus(RoomStatus.READY);
        room.addClient(new Client());
        return room;
    }
}
