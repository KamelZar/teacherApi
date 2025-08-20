package me.synology.techrevive.teacher.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class helloEndpoint {
    @GetMapping("/hello")
    public String get(){
        return "Hello World";
    }
}
