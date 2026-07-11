package com.kvn.schoolinvoices.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    private Long studentId;
    private String admissionNo;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dob;
    private String className;
    private String sectionName;
    private String status;
    private Long parentId;
    private String fullName;
}