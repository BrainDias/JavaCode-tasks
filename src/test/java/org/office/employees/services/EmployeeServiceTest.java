package org.office.employees.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.office.employees.entities.Employee;
import org.office.employees.projections.EmployeeProjection;
import org.office.employees.repositories.DepartmentRepository;
import org.office.employees.repositories.EmployeeRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setPosition("Developer");
        employee.setSalary(3000);
    }

    @Test
    public void testGetAllEmployees_Success() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee));

        List<Employee> employees = employeeService.getAllEmployees();

        assertThat(employees).hasSize(1);
        assertThat(employees.get(0).getFirstName()).isEqualTo("John");
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void testGetEmployeeById_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> foundEmployee = employeeService.getEmployeeById(1L);

        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getFirstName()).isEqualTo("John");
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Employee> foundEmployee = employeeService.getEmployeeById(999L);

        assertThat(foundEmployee).isEmpty();
        verify(employeeRepository, times(1)).findById(999L);
    }

    @Test
    public void testAddEmployee_Success() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee savedEmployee = employeeService.addEmployee(employee);

        assertThat(savedEmployee.getFirstName()).isEqualTo("John");
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    public void testUpdateEmployee_Success() {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setFirstName("Jane");
        updatedEmployee.setLastName("Doe");
        updatedEmployee.setPosition("Manager");
        updatedEmployee.setSalary(4000);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        Employee result = employeeService.updateEmployee(1L, updatedEmployee);

        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getPosition()).isEqualTo("Manager");
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployee_NotFound() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> employeeService.updateEmployee(999L, employee))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee not found");

        verify(employeeRepository, times(1)).findById(999L);
    }

    @Test
    public void testDeleteEmployee_Success() {
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetEmployeeProjections_Success() {
        EmployeeProjection projection = mock(EmployeeProjection.class);
        when(projection.getFullName()).thenReturn("John Doe");
        when(employeeRepository.findAllProjectedBy()).thenReturn(Arrays.asList(projection));

        List<EmployeeProjection> projections = employeeService.getEmployeeProjections();

        assertThat(projections).hasSize(1);
        assertThat(projections.get(0).getFullName()).isEqualTo("John Doe");
        verify(employeeRepository, times(1)).findAllProjectedBy();
    }
}
