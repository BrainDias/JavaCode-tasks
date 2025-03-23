package org.office.employees.repositories;

import org.office.employees.entities.Employee;
import org.office.employees.projections.EmployeeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e")
    List<EmployeeProjection> findAllProjectedBy();
}

