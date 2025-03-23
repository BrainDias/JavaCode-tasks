package org.office.employees.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String position;
    private double salary;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}

