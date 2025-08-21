package me.synology.techrevive.teacher.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import me.synology.techrevive.teacher.entities.Student;
import me.synology.techrevive.teacher.resources.dto.StudentRequest;
import me.synology.techrevive.teacher.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@Tag(name = "Student", description = "Student management endpoints")
@SecurityRequirement(name = "Google OAuth")
public class StudentEndpoint {

    @Autowired
    private StudentService studentService;

    @PostMapping
    @Operation(summary = "Create new student", description = "Create a new student with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Student created successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid or missing access token"),
        @ApiResponse(responseCode = "400", description = "Invalid student data")
    })
    public ResponseEntity<StudentRequest> addStudent(@RequestBody @Valid @NotNull StudentRequest studentRequest) {
        StudentRequest savedStudent = studentService.addStudent(studentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    @Operation(summary = "Get student by ID", description = "Retrieve a student by their unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student found successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid or missing access token"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<StudentRequest> getStudent(
            @Parameter(description = "Student ID") @PathVariable @Valid @NotNull Long id) {
        StudentRequest student = studentService.getStudent(id);
        return ResponseEntity.ok(student);
    }
    
    @GetMapping(produces = "application/json")
    @Operation(summary = "Get all students", description = "Retrieve a list of all students")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid or missing access token")
    })
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student", description = "Delete a student by their unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid or missing access token"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "Student ID") @PathVariable @Valid @NotNull Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
