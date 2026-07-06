package com.kvn.schoolinvoices.controller;


import com.kvn.schoolinvoices.dto.StudentDTO;
import com.kvn.schoolinvoices.service.StudentService;
import com.kvn.schoolinvoices.service.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/schooladmin")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public ResponseEntity<Page<StudentDTO>> getStudents(

            @RequestParam(required = false)
            String search,

            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "studentId",
                    direction = Sort.Direction.ASC)
            Pageable pageable) {

        return ResponseEntity.ok(
                studentService.searchStudents(
                        search,
                        pageable));
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {

        StudentDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<StudentDTO> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentDTO student) {

         StudentDTO updatedStudent =
                studentService.updateStudent(id, student);

        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("students/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(
            @PathVariable Long id) {

                studentService.deleteStudent(id);
        return ResponseEntity.ok(
                Map.of("message", "Student deleted successfully")
        );
     //   return ResponseEntity.ok("deletedStudent");
    }

    @PostMapping("/students")
    @PreAuthorize("hasRole('SCHOOL_ADMIN')")
    public ResponseEntity<StudentDTO> createStudent(
            @RequestBody StudentDTO studentDTO) {

        return ResponseEntity.ok(
                studentService.createStudent(studentDTO));
    }

}
