package com.gradetracker.service;

import com.gradetracker.dto.StudentRequest;
import com.gradetracker.dto.StudentResponse;
import com.gradetracker.entity.Student;
import com.gradetracker.exception.ConflictException;
import com.gradetracker.exception.ResourceNotFoundException;
import com.gradetracker.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public List<StudentResponse> findAll() {
        return studentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public StudentResponse create(StudentRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Student with email '" + request.getEmail() + "' already exists");
        }
        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        return toResponse(studentRepository.save(student));
    }

    public StudentResponse update(Long id, StudentRequest request) {
        Student student = getOrThrow(id);
        if (!student.getEmail().equals(request.getEmail())
                && studentRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email '" + request.getEmail() + "' is already taken");
        }
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        return toResponse(studentRepository.save(student));
    }

    public void delete(Long id) {
        getOrThrow(id);
        studentRepository.deleteById(id);
    }

    private Student getOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    private StudentResponse toResponse(Student s) {
        return new StudentResponse(s.getId(), s.getFirstName(), s.getLastName(), s.getEmail());
    }
}
