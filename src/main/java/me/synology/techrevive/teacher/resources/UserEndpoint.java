package me.synology.techrevive.teacher.resources;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import me.synology.techrevive.teacher.resources.dto.UserRequest;
import me.synology.techrevive.teacher.resources.dto.UserResponse;
import me.synology.techrevive.teacher.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserEndpoint {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<UserResponse> getUserFromToken(@RequestParam @NotNull String accessToken) {
        UserResponse user = userService.getUserFromToken(accessToken);
        return ResponseEntity.ok(user);
    }
    
    @PostMapping
    public ResponseEntity<UserResponse> createUserFromToken(@RequestBody @Valid @NotNull UserRequest userRequest) {
        UserResponse user = userService.createUserFromToken(userRequest.accessToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}