package com.kvn.schoolinvoices.service;

import com.kvn.schoolinvoices.AppUser;
import com.kvn.schoolinvoices.UserRepository;
import com.kvn.schoolinvoices.dto.AppUserDto;
import com.kvn.schoolinvoices.dto.InvoiceDTO;
import com.kvn.schoolinvoices.dto.StudentDTO;
import com.kvn.schoolinvoices.entity.Invoice;
import com.kvn.schoolinvoices.entity.Parent;
import com.kvn.schoolinvoices.entity.Student;
import com.kvn.schoolinvoices.entity.StudentStatus;
import com.kvn.schoolinvoices.service.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;


    public Page<StudentDTO> searchStudents(
            String search,
            Pageable pageable) {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();


        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (user.getRole()!=null && user.getRole().equals("parent")){
            return studentRepository
                    .searchStudentsByUser(search,user.getId(), pageable)
                    .map(this::convertToDto);
        }
        else{
            return studentRepository
                    .searchStudents(search, pageable)
                    .map(this::convertToDto);
        }
    }


    private StudentDTO convertToDto(Student student) {


        return StudentDTO.builder()
                .studentId(student.getStudentId())
                .admissionNo(student.getAdmissionNo())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .gender(student.getGender())
                .parentName(        student.getUser().getFullName())
                .parentId(student.getUser().getId())

            //    .dob(LocalDate.parse(student.getDob()))
                .className(student.getClassName())
                .sectionName(student.getSectionName())
                .status(student.getStatus().name())
            //    .invoiceDTOList(convertInvoicestDTOList(student.getInvoices()))
               .build();
    }

    private List<InvoiceDTO> convertInvoicestDTOList(List<Invoice> invoices) {
        return invoices.stream()
                .map(this::convertToDto1)
                .collect(Collectors.toList());
    }

    private InvoiceDTO convertToDto1(Invoice invoice) {
        return InvoiceDTO.builder().invoiceID(invoice.getInvoiceId()).invoiceDate(invoice.getInvoiceDate()).
                dueDate(invoice.getDueDate()).invoiceNumber(invoice.getInvoiceNumber()).build();
    }

    public StudentDTO getStudentById(Long id) {

        return studentRepository.findById(id).map(this::convertToDto)
                .orElseThrow(() ->
                        new RuntimeException("Student not found with id: " + id));
    }

    public StudentDTO updateStudent(Long id, StudentDTO student) {

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Student not found with id: " + id));

        existingStudent.setAdmissionNo(student.getAdmissionNo());
        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setGender(student.getGender());
    //    existingStudent.setDob(String.valueOf(student.getDob()));
        existingStudent.setClassName(student.getClassName());
        existingStudent.setSectionName(student.getSectionName());
        existingStudent.setStatus(StudentStatus.valueOf(student.getStatus()));

        studentRepository.save(existingStudent);

        return student;
    }

    public StudentDTO createStudent(StudentDTO dto) {

        if (studentRepository.existsByAdmissionNo(dto.getAdmissionNo())) {
            throw new RuntimeException("Admission Number already exists");
        }

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Student student = new Student();
        student.setAdmissionNo(dto.getAdmissionNo());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setGender(dto.getGender());
     //   student.setDob(String.valueOf(dto.getDob()));
        student.setClassName(dto.getClassName());
        student.setSectionName(dto.getSectionName());
        student.setStatus(StudentStatus.valueOf(dto.getStatus()));
        student.setCreatedBy(email);
    //    Parent parent = new Parent();
     //   parent.setParentId(dto.getParentId());
    //    student.setParent(parent);
     //  Optional<AppUser> user1=  userRepository.findById(dto.getParentId());

       AppUser appUser = new AppUser();
       appUser.setId(dto.getParentId());
        student.setUser(appUser);

        Student savedStudent = studentRepository.save(student);

        dto.setStudentId(savedStudent.getStudentId());

        return dto;
    }

    @Transactional
    public  void deleteStudent(Long id){
        studentRepository.deleteById(id);
    }

    public Page<StudentDTO> searchStudents(StudentDTO searchDTO, Pageable pageable) {
        Page<StudentDTO> map = studentRepository.findByAdmissionNoContainingIgnoreCaseAndFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
                searchDTO.getAdmissionNo(), searchDTO.getFirstName(), searchDTO.getLastName()
                , pageable
        ).map(student -> new StudentDTO(student.getStudentId(), student.getAdmissionNo(), student.getFirstName(), student.getLastName(),
                student.getClassName(), student.getSectionName(),student.getUser().getFullName()));
        return map;

    }


   /* public Page<StudentDTO> searchParents(StudentDTO searchDTO, Pageable pageable) {
        return   studentRepository.findByAdmissionNoContainingIgnoreCaseAndFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
                searchDTO.getAdmissionNo(), searchDTO.getFirstName(), searchDTO.getLastName(),
                , pageable
        ).map(student -> new StudentDTO(student.getStudentId(),student.getAdmissionNo(), student.getFirstName(), student.getLastName(),
                student.getClassName(),student.getSectionName());

        return null;
    }*/
}