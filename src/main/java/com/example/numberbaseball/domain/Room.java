package com.example.numberbaseball.domain;

import com.example.numberbaseball.Enum.RoomStatus;
import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public boolean doAllUsersReady() {
        return this.userList.stream()
                .allMatch(User::isReady);
    }

    public User getRandomUser() {
        return this.userList.get(0);
    }

    public User getCounterUser(User user) {
        Optional<User> counterUser = this.userList.stream()
                .filter(tmpUser -> !tmpUser.equals(user))
                .findAny();

        if (counterUser.isEmpty()) {
           throw new RuntimeException("there is no CounterUser in this Room");
        }

        return counterUser.get();
    }
}
