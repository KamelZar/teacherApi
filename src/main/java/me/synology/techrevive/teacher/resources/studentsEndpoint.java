package me.synology.techrevive.teacher.resources;

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
public class studentsEndpoint {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentRequest> addStudent(@RequestBody @Valid @NotNull StudentRequest studentRequest) {
        StudentRequest savedStudent = studentService.addStudent(studentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<StudentRequest> getStudent(@PathVariable @Valid @NotNull Long id) {
        StudentRequest student = studentService.getStudent(id);
        return ResponseEntity.ok(student);
    }
    
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable @Valid @NotNull Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
