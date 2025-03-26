package org.office.employees.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.office.employees.entities.Department;
import org.office.employees.repositories.DepartmentRepository;
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
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private DepartmentRepository departmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllDepartments_Success() throws Exception {
        List<Department> departments = Arrays.asList(
                new Department(1L, "HR"),
                new Department(2L, "IT")
        );

        given(departmentRepository.findAll()).willReturn(departments);

        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("HR"))
                .andExpect(jsonPath("$[1].name").value("IT"));
    }

    @Test
    public void testGetDepartmentById_Success() throws Exception {
        Department department = new Department(1L, "Finance");

        given(departmentRepository.findById(1L)).willReturn(Optional.of(department));

        mockMvc.perform(get("/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Finance"));
    }

    @Test
    public void testGetDepartmentById_NotFound() throws Exception {
        given(departmentRepository.findById(999L)).willReturn(Optional.empty());

        mockMvc.perform(get("/departments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddDepartment_Success() throws Exception {
        Department department = new Department(1L, "Marketing");

        given(departmentRepository.save(any(Department.class))).willReturn(department);

        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(department)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Marketing"));
    }

    @Test
    public void testDeleteDepartment_Success() throws Exception {
        doNothing().when(departmentRepository).deleteById(1L);

        mockMvc.perform(delete("/departments/1"))
                .andExpect(status().isOk());

        verify(departmentRepository, times(1)).deleteById(1L);
    }
}
