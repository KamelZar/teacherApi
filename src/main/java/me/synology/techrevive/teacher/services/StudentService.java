package me.synology.techrevive.teacher.services;

import jakarta.validation.Valid;
import me.synology.techrevive.teacher.entities.Student;
import me.synology.techrevive.teacher.exceptions.NotFoundException;
import me.synology.techrevive.teacher.repositories.StudentRepository;
import me.synology.techrevive.teacher.resources.dto.StudentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    public StudentRequest getStudent(@Valid Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            Student s = student.get();
            return new StudentRequest(s.getFirstName(), s.getLastName(), s.getBirthDate());
        }
        throw new NotFoundException("Student not found with id: " + id);
    }
    
    public StudentRequest addStudent(StudentRequest studentRequest) {
        Student student = new Student(
            studentRequest.firstName(),
            studentRequest.lastName(), 
            studentRequest.birthDate()
        );
        Student savedStudent = studentRepository.save(student);
        return new StudentRequest(
            savedStudent.getFirstName(),
            savedStudent.getLastName(),
            savedStudent.getBirthDate()
        );
    }
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new NotFoundException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
}
