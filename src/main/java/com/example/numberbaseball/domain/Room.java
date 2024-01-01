package com.example.numberbaseball.domain;

import com.example.numberbaseball.Enum.RoomStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Room {

    private String code;
    private RoomStatus roomStatus;
    private List<Client> clientList ;
    private List<User> userList;

    public Room(RoomStatus roomStatus) {
        this.code = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        this.clientList = new ArrayList<>();
        this.roomStatus = roomStatus;
        this.userList = new ArrayList<>();
    }


    public List<Client> addClient(Client client) {
        this.clientList.add(client);
        return clientList;
    }

    public void addUser(User user) {
        this.userList.add(user);
    }

    public String getCode(){
        return this.code;
    }

    public void setRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

}
