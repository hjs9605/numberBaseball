package com.example.numberbaseball.repository;

import com.example.numberbaseball.domain.User;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class UserRepository {

    private static Map<String, User> userMap;

    @PostConstruct
    void init() {
        userMap = new HashMap<>();
    }

    public void save(User user) {
        if(userMap.containsKey(user.getSessionId())) {
            throw new IllegalArgumentException("이미 닉네임이 설정되어있습니다");
        }
        userMap.put(user.getSessionId(), user);
        log.info(userMap.get(user.getSessionId()).getSessionId());
    }

    public User findBySessionId(String sessionId) {
        if(!userMap.containsKey(sessionId)){
            throw new IllegalArgumentException("sessionId에 대한 user가 없습니다.");
        }
        return userMap.get(sessionId);
    }
}
