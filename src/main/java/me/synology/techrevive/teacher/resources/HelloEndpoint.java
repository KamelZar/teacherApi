package me.synology.techrevive.teacher.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Hello", description = "Public hello endpoint")
public class HelloEndpoint {
    
    @GetMapping("/hello")
    @Operation(summary = "Get hello message", description = "Returns a simple hello world message")
    @ApiResponse(responseCode = "200", description = "Hello message returned successfully")
    public String get(){
        return "Hello World";
    }
}
