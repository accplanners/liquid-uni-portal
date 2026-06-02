package com.acc.ACC_AYCE.Entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "registrars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Registrar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrarId;

    private String name;
    private String office;

    // One registrar manages many students
    @OneToMany(mappedBy = "registrar")
    private List<Student> students;

}