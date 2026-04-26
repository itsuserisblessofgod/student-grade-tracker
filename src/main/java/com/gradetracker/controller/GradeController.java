package com.gradetracker.controller;

import com.gradetracker.dto.GradeRequest;
import com.gradetracker.dto.GradeResponse;
import com.gradetracker.service.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @GetMapping
    public List<GradeResponse> getAll() {
        return gradeService.findAll();
    }

    @GetMapping("/{id}")
    public GradeResponse getById(@PathVariable Long id) {
        return gradeService.findById(id);
    }

    @PostMapping
    public ResponseEntity<GradeResponse> create(@Valid @RequestBody GradeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gradeService.create(request));
    }

    @PutMapping("/{id}")
    public GradeResponse update(@PathVariable Long id, @Valid @RequestBody GradeRequest request) {
        return gradeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gradeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
