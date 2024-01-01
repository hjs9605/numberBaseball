package com.example.numberbaseball.Handler;

import com.example.numberbaseball.exception.RoomFullException;
import com.example.numberbaseball.exception.RoomNotFoundException;
import com.example.numberbaseball.exception.RoomNotReadyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
@Slf4j(topic = "GlobalExceptionHandler")
public class GlobalExceptionHandler {

    @ExceptionHandler(RoomFullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleRoomFullException() {
        return new ResponseEntity<>("방 꽉 찼습니다~", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleRoomNotFoundException() {
        return new ResponseEntity<>("방 코드의 방이 없습니다.", HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(RoomNotReadyException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleRoomNotReadyException() {
        return new ResponseEntity<>("상대가 들어오지 않았습니다.", HttpStatus.BAD_REQUEST);
    }

}