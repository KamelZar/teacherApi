package me.synology.techrevive.teacher.resources;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import me.synology.techrevive.teacher.resources.dto.PostRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class helloEndpoint {
    @GetMapping("/hello")
    public String get(){
        return "Hello World";
    }

    @PostMapping("/something")
    public Double doSomething(@RequestBody @Valid @NotNull PostRequest postRequest){
        return  postRequest.value() * 3.5;
    }

}
