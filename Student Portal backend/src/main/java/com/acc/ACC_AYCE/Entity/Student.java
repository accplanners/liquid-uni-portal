package com.acc.ACC_AYCE.Entity;

import com.acc.ACC_AYCE.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;   // Correct Primary Key

    private String name;
    private String email;
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    private String studentCode;
    private String course;
    private int year;
    private String status;
    private String type;
    private double gpa;
    private int attendance;
    
    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToMany
    @JoinTable(
        name = "student_courses",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnore
    private Billing billing;

    @ManyToOne
    @JoinColumn(name = "registrar_id")
    private Registrar registrar;
}