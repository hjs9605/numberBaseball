package com.example.numberbaseball.server;

import com.example.numberbaseball.domain.Client;
import com.example.numberbaseball.domain.GameRoom;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameServer {

    private static final List<GameRoom> gameRooms = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("서버가 시작되었습니다. 클라이언트 연결 대기 중...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client 연결.");

                // 각 클라이언트에 대한 새로운 스레드 시작
                GameRoom gameRoom = new GameRoom();
                gameRooms.add(gameRoom);
                Client client = new Client(clientSocket, gameRoom);

                Thread thread = new Thread(client);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }


    }
}

