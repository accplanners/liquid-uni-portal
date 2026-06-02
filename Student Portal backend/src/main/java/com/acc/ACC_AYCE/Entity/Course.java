package com.acc.ACC_AYCE.Entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    private String title;
    private int credits;

    // Many students enroll in many courses
    @ManyToMany(mappedBy = "courses")
    private List<Student> students;

    // Many courses taught by one faculty
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    // Many courses belong to one catalogue
    @ManyToOne
    @JoinColumn(name = "catalogue_id")
    private CourseCatalogue catalogue;

}