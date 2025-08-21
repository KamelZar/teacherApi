package me.synology.techrevive.teacher.services;

import me.synology.techrevive.teacher.entities.ClassEntity;
import me.synology.techrevive.teacher.entities.User;
import me.synology.techrevive.teacher.exceptions.NotFoundException;
import me.synology.techrevive.teacher.repositories.ClassRepository;
import me.synology.techrevive.teacher.repositories.UserRepository;
import me.synology.techrevive.teacher.services.dto.ClassData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassService {
    
    @Autowired
    private ClassRepository classRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Transactional
    public ClassData createClass(String name, String description, Integer schoolYear, String googleId) {
        User teacher = userRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new NotFoundException("Teacher not found with Google ID: " + googleId));
        
        if (classRepository.existsByNameAndSchoolYear(name, schoolYear)) {
            throw new IllegalArgumentException("A class with name '" + name + "' already exists for school year " + schoolYear);
        }
        
        ClassEntity classEntity = new ClassEntity();
        classEntity.setName(name);
        classEntity.setDescription(description);
        classEntity.setSchoolYear(schoolYear);
        classEntity.setCreatedAt(LocalDateTime.now());
        classEntity.setUpdatedAt(LocalDateTime.now());
        
        ClassEntity savedClass = classRepository.save(classEntity);
        
        return ClassData.from(savedClass);
    }
    
    @Transactional(readOnly = true)
    public List<ClassData> getClassesByTeacher(String googleId) {
        User teacher = userRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new NotFoundException("Teacher not found with Google ID: " + googleId));
        
        List<ClassEntity> classes = classRepository.findClassesByTeacherId(teacher.getId());
        
        return classes.stream()
                .map(ClassData::from)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ClassData getClassById(Long classId, String googleId) {
        User user = userRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new NotFoundException("User not found with Google ID: " + googleId));
        
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new NotFoundException("Class not found with ID: " + classId));
        
        List<ClassEntity> userClasses = classRepository.findClassesByTeacherId(user.getId());
        boolean hasAccess = userClasses.stream().anyMatch(c -> c.getId().equals(classId));
        
        if (!hasAccess) {
            throw new IllegalAccessError("Access denied to class ID: " + classId);
        }
        
        return ClassData.from(classEntity);
    }
    
    @Transactional(readOnly = true)
    public List<ClassData> getClassesByStudent(String googleId) {
        User student = userRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new NotFoundException("Student not found with Google ID: " + googleId));
        
        List<ClassEntity> classes = classRepository.findClassesByStudentId(student.getId());
        
        return classes.stream()
                .map(ClassData::from)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ClassData> getClassesBySchoolYear(Integer schoolYear) {
        List<ClassEntity> classes = classRepository.findBySchoolYear(schoolYear);
        
        return classes.stream()
                .map(ClassData::from)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ClassData updateClass(Long classId, String name, String description, String googleId) {
        User teacher = userRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new NotFoundException("Teacher not found with Google ID: " + googleId));
        
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new NotFoundException("Class not found with ID: " + classId));
        
        List<ClassEntity> teacherClasses = classRepository.findClassesByTeacherId(teacher.getId());
        boolean hasAccess = teacherClasses.stream().anyMatch(c -> c.getId().equals(classId));
        
        if (!hasAccess) {
            throw new IllegalAccessError("Access denied to modify class ID: " + classId);
        }
        
        if (name != null && !name.equals(classEntity.getName())) {
            if (classRepository.existsByNameAndSchoolYear(name, classEntity.getSchoolYear())) {
                throw new IllegalArgumentException("A class with name '" + name + "' already exists for school year " + classEntity.getSchoolYear());
            }
            classEntity.setName(name);
        }
        
        if (description != null) {
            classEntity.setDescription(description);
        }
        
        classEntity.setUpdatedAt(LocalDateTime.now());
        ClassEntity savedClass = classRepository.save(classEntity);
        
        return ClassData.from(savedClass);
    }
}