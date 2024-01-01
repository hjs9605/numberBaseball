package com.example.numberbaseball.repository;

import com.example.numberbaseball.domain.Room;
import com.example.numberbaseball.exception.RoomAlreadyExistsException;
import com.example.numberbaseball.exception.RoomNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class RoomRepository {
    private static Map<String, Room> roomMap;

    @PostConstruct
    void init(){
        roomMap = new HashMap<>();
    }

    public void save(Room room){
        if(roomMap.containsKey(room.getCode())){
            throw new RoomAlreadyExistsException();
        }
        roomMap.put(room.getCode(), room);
        log.info(roomMap.get(room.getCode()).getCode());
    }
    public Room findByCode(String code){
        if(roomMap.containsKey(code)){
            return roomMap.get(code);
        }
        throw new RoomNotFoundException();
    }
}
