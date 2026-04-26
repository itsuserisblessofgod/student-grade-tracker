package com.gradetracker.repository;

import com.gradetracker.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentId(Long studentId);
    List<Grade> findByCourseId(Long courseId);
    boolean existsByStudentIdAndCourseIdAndSemester(Long studentId, Long courseId, String semester);

    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.student.id = :studentId")
    Double findAverageScoreByStudentId(Long studentId);
}
