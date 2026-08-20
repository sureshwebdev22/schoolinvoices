package com.kvn.schoolinvoices.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "students1")
@Getter
@Setter
@Builder
public class Student1 {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rollNo;
    private String standard;
    private String name;

    public Student1() {

    }

    public Student1(Long id, String rollNo, String name, String standard) {
        this.id = id;
        this.rollNo = rollNo;
        this.name = name;
        this.standard = standard;
    }

    // Getters and setters

}
