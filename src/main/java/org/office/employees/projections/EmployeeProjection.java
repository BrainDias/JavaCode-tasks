package org.office.employees.projections;

import org.office.employees.entities.Department;

public interface EmployeeProjection {
    default String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    String getFirstName();
    String getLastName();
    String getPosition();

    default String getDepartmentName() {
        return getDepartment().getName();
    }

    Department getDepartment();
}

