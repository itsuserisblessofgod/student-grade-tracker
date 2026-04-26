package com.gradetracker.controller;

import com.gradetracker.dto.GradeResponse;
import com.gradetracker.dto.StudentRequest;
import com.gradetracker.dto.StudentResponse;
import com.gradetracker.service.GradeService;
import com.gradetracker.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final GradeService gradeService;

    @GetMapping
    public List<StudentResponse> getAll() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public StudentResponse getById(@PathVariable Long id) {
        return studentService.findById(id);
    }

    @PostMapping
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody StudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(request));
    }

    @PutMapping("/{id}")
    public StudentResponse update(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return studentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/grades")
    public List<GradeResponse> getGrades(@PathVariable Long id) {
        return gradeService.findByStudent(id);
    }

    @GetMapping("/{id}/average")
    public ResponseEntity<Map<String, Object>> getAverage(@PathVariable Long id) {
        Double avg = gradeService.getStudentAverage(id);
        return ResponseEntity.ok(Map.of(
                "studentId", id,
                "average", avg != null ? avg : 0.0
        ));
    }
}
