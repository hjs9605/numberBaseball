package com.example.numberbaseball.controller;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@NoArgsConstructor
public class PageController {

    @GetMapping("/")
    public String mainBuilder(){
        return "mainPage";
    }
    @GetMapping("/main")
    public String mainBuilder2() {
        return "mainPage";
    }

    @GetMapping("/room/{roomCode}")
    public String roomPageBuilder(@PathVariable String roomCode){
        return "roomPage";
    }
}
