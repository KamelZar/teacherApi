package me.synology.techrevive.teacher.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import me.synology.techrevive.teacher.resources.dto.ClassRequest;
import me.synology.techrevive.teacher.resources.dto.ClassResponse;
import me.synology.techrevive.teacher.resources.dto.ClassUpdateRequest;
import me.synology.techrevive.teacher.services.ClassService;
import me.synology.techrevive.teacher.services.UserService;
import me.synology.techrevive.teacher.services.dto.ClassData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@Tag(name = "Classes", description = "Class management endpoints")
@SecurityRequirement(name = "bearer-key")
public class ClassEndpoint {
    
    @Autowired
    private ClassService classService;
    
    @Autowired
    private UserService userService;
    
    @Operation(summary = "Create a new class", description = "Create a new class (teachers only)")
    @PostMapping
    public ResponseEntity<ClassResponse> createClass(
            @Valid @RequestBody ClassRequest request,
            @RequestHeader("Authorization") String authHeader) {
        
        String token = extractToken(authHeader);
        String googleId = getGoogleIdFromToken(token);
        
        ClassData classData = classService.createClass(
            request.name(),
            request.description(),
            request.schoolYear(),
            googleId
        );
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ClassResponse.from(classData));
    }
    
    @Operation(summary = "Get teacher's classes", description = "Get all classes for the authenticated teacher")
    @GetMapping
    public ResponseEntity<List<ClassResponse>> getClasses(
            @RequestHeader("Authorization") String authHeader) {
        
        String token = extractToken(authHeader);
        String googleId = getGoogleIdFromToken(token);
        
        List<ClassData> classes = classService.getClassesByTeacher(googleId);
        List<ClassResponse> response = classes.stream()
            .map(ClassResponse::from)
            .toList();
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get class by ID", description = "Get specific class details")
    @GetMapping("/{id}")
    public ResponseEntity<ClassResponse> getClass(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        
        String token = extractToken(authHeader);
        String googleId = getGoogleIdFromToken(token);
        
        ClassData classData = classService.getClassById(id, googleId);
        return ResponseEntity.ok(ClassResponse.from(classData));
    }
    
    @Operation(summary = "Update class", description = "Update class information (teachers only)")
    @PutMapping("/{id}")
    public ResponseEntity<ClassResponse> updateClass(
            @PathVariable Long id,
            @Valid @RequestBody ClassUpdateRequest request,
            @RequestHeader("Authorization") String authHeader) {
        
        String token = extractToken(authHeader);
        String googleId = getGoogleIdFromToken(token);
        
        ClassData classData = classService.updateClass(
            id,
            request.name(),
            request.description(),
            googleId
        );
        
        return ResponseEntity.ok(ClassResponse.from(classData));
    }
    
    @Operation(summary = "Get classes by school year", description = "Get all classes for a specific school year")
    @GetMapping("/school-year/{year}")
    public ResponseEntity<List<ClassResponse>> getClassesBySchoolYear(
            @PathVariable Integer year,
            @RequestHeader("Authorization") String authHeader) {
        
        String token = extractToken(authHeader);
        validateToken(token);
        
        List<ClassData> classes = classService.getClassesBySchoolYear(year);
        List<ClassResponse> response = classes.stream()
            .map(ClassResponse::from)
            .toList();
        
        return ResponseEntity.ok(response);
    }
    
    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        return authHeader.substring(7);
    }
    
    private String getGoogleIdFromToken(String token) {
        return userService.getGoogleIdFromToken(token);
    }
    
    private void validateToken(String token) {
        if (!userService.validateToken(token)) {
            throw new IllegalArgumentException("Invalid token");
        }
    }
}