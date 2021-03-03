package com.example.firstSpringProgect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
public class TestController {

    @GetMapping("/t/1")
    public String t(
            @RequestParam String sas
    ){
        return sas;
    }

    @GetMapping("/t/2")
    public String t2(
            @RequestBody String sas
    ){
        return sas;
    }

    @PostMapping("/t/3")
    public String t3(
            @RequestBody String sas
    ){
        return sas;
    }
}
