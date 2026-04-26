package com.gradetracker.service;

import com.gradetracker.dto.GradeRequest;
import com.gradetracker.dto.GradeResponse;
import com.gradetracker.entity.Course;
import com.gradetracker.entity.Grade;
import com.gradetracker.entity.Student;
import com.gradetracker.exception.ConflictException;
import com.gradetracker.exception.ResourceNotFoundException;
import com.gradetracker.repository.CourseRepository;
import com.gradetracker.repository.GradeRepository;
import com.gradetracker.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public List<GradeResponse> findAll() {
        return gradeRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public GradeResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<GradeResponse> findByStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }
        return gradeRepository.findByStudentId(studentId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<GradeResponse> findByCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }
        return gradeRepository.findByCourseId(courseId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public Double getStudentAverage(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }
        return gradeRepository.findAverageScoreByStudentId(studentId);
    }

    public GradeResponse create(GradeRequest request) {
        if (gradeRepository.existsByStudentIdAndCourseIdAndSemester(
                request.getStudentId(), request.getCourseId(), request.getSemester())) {
            throw new ConflictException("Grade already exists for this student, course, and semester");
        }
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setCourse(course);
        grade.setScore(request.getScore());
        grade.setSemester(request.getSemester());
        return toResponse(gradeRepository.save(grade));
    }

    public GradeResponse update(Long id, GradeRequest request) {
        Grade grade = getOrThrow(id);

        boolean semesterOrCourseChanged = !grade.getSemester().equals(request.getSemester())
                || !grade.getCourse().getId().equals(request.getCourseId())
                || !grade.getStudent().getId().equals(request.getStudentId());

        if (semesterOrCourseChanged && gradeRepository.existsByStudentIdAndCourseIdAndSemester(
                request.getStudentId(), request.getCourseId(), request.getSemester())) {
            throw new ConflictException("Grade already exists for this student, course, and semester");
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        grade.setStudent(student);
        grade.setCourse(course);
        grade.setScore(request.getScore());
        grade.setSemester(request.getSemester());
        return toResponse(gradeRepository.save(grade));
    }

    public void delete(Long id) {
        getOrThrow(id);
        gradeRepository.deleteById(id);
    }

    private Grade getOrThrow(Long id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id: " + id));
    }

    private GradeResponse toResponse(Grade g) {
        String letter = toLetter(g.getScore());
        return new GradeResponse(
                g.getId(),
                g.getStudent().getId(),
                g.getStudent().getFirstName() + " " + g.getStudent().getLastName(),
                g.getCourse().getId(),
                g.getCourse().getName(),
                g.getScore(),
                letter,
                g.getSemester()
        );
    }

    private String toLetter(double score) {
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }
}
