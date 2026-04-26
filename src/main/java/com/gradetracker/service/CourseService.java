package com.gradetracker.service;

import com.gradetracker.dto.CourseRequest;
import com.gradetracker.dto.CourseResponse;
import com.gradetracker.entity.Course;
import com.gradetracker.exception.ConflictException;
import com.gradetracker.exception.ResourceNotFoundException;
import com.gradetracker.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public List<CourseResponse> findAll() {
        return courseRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public CourseResponse create(CourseRequest request) {
        if (courseRepository.existsByName(request.getName())) {
            throw new ConflictException("Course '" + request.getName() + "' already exists");
        }
        Course course = new Course();
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());
        return toResponse(courseRepository.save(course));
    }

    public CourseResponse update(Long id, CourseRequest request) {
        Course course = getOrThrow(id);
        if (!course.getName().equals(request.getName())
                && courseRepository.existsByName(request.getName())) {
            throw new ConflictException("Course name '" + request.getName() + "' is already taken");
        }
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());
        return toResponse(courseRepository.save(course));
    }

    public void delete(Long id) {
        getOrThrow(id);
        courseRepository.deleteById(id);
    }

    private Course getOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    private CourseResponse toResponse(Course c) {
        return new CourseResponse(c.getId(), c.getName(), c.getDescription(), c.getCredits());
    }
}
