package com.infinbank.rest.repository;

import com.infinbank.rest.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("SELECT c FROM Course c JOIN c.teachers t WHERE t.id = :teacherId")
    Optional<Course> findByTeacherId(@Param("teacherId") Integer teacherId);
}
