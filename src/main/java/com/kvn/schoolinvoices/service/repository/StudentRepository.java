package com.kvn.schoolinvoices.service.repository;

import com.kvn.schoolinvoices.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByParentParentId(Long parentId);

    Student findByAdmissionNo(String admissionNo);

    @Query("""
            SELECT s
            FROM Student s
            WHERE (:search IS NULL OR
                   LOWER(s.admissionNo) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(s.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(s.className) LIKE LOWER(CONCAT('%', :search, '%'))
                  )
            """)
    Page<Student> searchStudents(
            @Param("search") String search,
            Pageable pageable);

    boolean existsByAdmissionNo(String admissionNo);

}