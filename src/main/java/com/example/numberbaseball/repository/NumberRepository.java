package com.example.numberbaseball.repository;

import com.example.numberbaseball.domain.UserNumber;
import com.example.numberbaseball.exception.NumberAlreadySetException;
import com.example.numberbaseball.exception.NumberNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class NumberRepository {

    private static Map<String, UserNumber> numberMap;

    @PostConstruct
    void init(){
        numberMap = new HashMap<>();
    }

    public void save(UserNumber number){
        if(numberMap.containsKey(number.getSessionId())){
            throw new NumberAlreadySetException();
        }
        numberMap.put(number.getSessionId(), number);
        log.info(numberMap.get(number.getSessionId()).getSessionId());
    }
    public UserNumber findBySessionId(String sessionId){
        if(numberMap.containsKey(sessionId)){
            return numberMap.get(sessionId);
        }
        throw new NumberNotFoundException();
    }
}
