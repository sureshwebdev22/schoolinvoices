package com.kvn.schoolinvoices.service;

import com.kvn.schoolinvoices.AppUser;
import com.kvn.schoolinvoices.UserRepository;
import com.kvn.schoolinvoices.dto.StudentDTO;
import com.kvn.schoolinvoices.entity.Parent;
import com.kvn.schoolinvoices.entity.Student;
import com.kvn.schoolinvoices.entity.StudentStatus;
import com.kvn.schoolinvoices.service.repository.ParentRepository;
import com.kvn.schoolinvoices.service.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private UserRepository userRepository;


    public Page<StudentDTO> searchStudents(
            String search,
            Pageable pageable) {

        return studentRepository
                .searchStudents(search, pageable)
                .map(this::convertToDto);
    }

    private StudentDTO convertToDto(Student student) {

        return StudentDTO.builder()
                .studentId(student.getStudentId())
                .admissionNo(student.getAdmissionNo())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .gender(student.getGender())
                .dob(student.getDob())
                .className(student.getClassName())
                .sectionName(student.getSectionName())
                .status(student.getStatus().name())
         //       .parentName(student.getParent().getFatherName() +"," + student.getParent().getMotherName())
           //     .parentId(student.getParent().getParentId())
                .build();
    }

  /*  public StudentDTO getStudentById(Long id) {
       Optional<Student> student = studentRepository.findById(id);
   //    Parent parent = student.get().getParent();
       System.out.println("father : "+parent.getFatherName());


        return studentRepository.findById(id).map(this::convertToDto)
                .orElseThrow(() ->
                        new RuntimeException("Student not found with id: " + id));
    } */

  /*  public StudentDTO updateStudent(Long id, StudentDTO student) {

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Student not found with id: " + id));

        existingStudent.setAdmissionNo(student.getAdmissionNo());
        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setGender(student.getGender());
        existingStudent.setDob(student.getDob());
        existingStudent.setClassName(student.getClassName());
        existingStudent.setSectionName(student.getSectionName());
        existingStudent.setStatus(StudentStatus.valueOf(student.getStatus()));

       Parent parent = existingStudent.getParent();
       String[] parentsName = student.getParentName().split(",");
       parent.setFatherName(parentsName[0]);
       parent.setMotherName(parentsName[1]);

        studentRepository.save(existingStudent);

        return student;
    } */

    public StudentDTO createStudent(StudentDTO dto) {

        if (studentRepository.existsByAdmissionNo(dto.getAdmissionNo())) {
            throw new RuntimeException("Admission Number already exists");
        }

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();


      //  AppUser user = userRepository.findByEmail(email)
          //      .orElseThrow(() -> new RuntimeException("User not found"));

        Student student = new Student();
        student.setAdmissionNo(dto.getAdmissionNo());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setGender(dto.getGender());
        student.setDob(dto.getDob());
        student.setClassName(dto.getClassName());
        student.setSectionName(dto.getSectionName());
        student.setStatus(StudentStatus.valueOf(dto.getStatus()));
        student.setCreatedBy(email);
        AppUser appUser = new AppUser();
        appUser.setId(dto.getParentId());
        appUser.setCreatedBy(email);
      /*  Parent parent = new Parent();
        parent.setParentId(dto.getParentId());
        student.setParent(parent); */
        student.setUser(appUser);
    //    student.set

        Student savedStudent = studentRepository.save(student);

        dto.setStudentId(savedStudent.getStudentId());

        return dto;
    }

    @Transactional
    public  void deleteStudent(Long id){
        studentRepository.deleteById(id);
    }


}