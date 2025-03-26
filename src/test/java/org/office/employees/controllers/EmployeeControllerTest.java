package org.office.employees.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.office.employees.entities.Department;
import org.office.employees.entities.Employee;
import org.office.employees.projections.EmployeeProjection;
import org.office.employees.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    private Department department;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        department = new Department();
    }

    @Test
    public void testGetAllEmployees_Success() throws Exception {
        List<Employee> employees = Arrays.asList(
                new Employee(1L, "John", "Doe", "Developer", 3000, department),
                new Employee(2L, "Jane", "Smith", "Designer", 3500, department)
        );

        given(employeeService.getAllEmployees()).willReturn(employees);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));
    }

    @Test
    public void testGetEmployeeById_Success() throws Exception {
        Employee employee = new Employee(1L, "John", "Doe", "Developer", 3000, department);

        given(employeeService.getEmployeeById(1L)).willReturn(Optional.of(employee));

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void testGetEmployeeById_NotFound() throws Exception {
        given(employeeService.getEmployeeById(999L)).willReturn(Optional.empty());

        mockMvc.perform(get("/employees/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddEmployee_Success() throws Exception {
        Employee employee = new Employee(1L, "Alice", "Brown", "Manager", 4000, department);

        given(employeeService.addEmployee(any(Employee.class))).willReturn(employee);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Alice Brown"));
    }

    @Test
    public void testUpdateEmployee_Success() throws Exception {
        Employee updatedEmployee = new Employee(1L, "Alice", "Brown", "Senior Manager", 5000, department);

        given(employeeService.updateEmployee(anyLong(), any(Employee.class))).willReturn(updatedEmployee);
        mockMvc.perform(put("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Brown"))
                .andExpect(jsonPath("$.position").value("Senior Manager"))
                .andExpect(jsonPath("$.salary").value(5000));
    }

    @Test
    public void testDeleteEmployee_Success() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isOk());

        verify(employeeService, times(1)).deleteEmployee(1L);
    }

    @Test
    public void testGetEmployeeProjections_Success() throws Exception {
        EmployeeProjection projection1 = mock(EmployeeProjection.class);
        EmployeeProjection projection2 = mock(EmployeeProjection.class);

        given(projection1.getFullName()).willReturn("John Doe");
        given(projection2.getFullName()).willReturn("Jane Smith");

        given(employeeService.getEmployeeProjections()).willReturn(Arrays.asList(projection1, projection2));

        mockMvc.perform(get("/employees/projections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));
    }
}
