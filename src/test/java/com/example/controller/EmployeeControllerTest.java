package com.example.controller;

import com.example.domain.Employee;
import com.example.domain.JwtRequest;
import com.example.repository.EmployeeRepository;
import com.example.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    String token = null;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    Employee testemp = null;

    @Before
    public void setUp() throws Exception {
        testemp = new Employee("test", "test", "test");
        testemp.setId(1L);
        String encyptPassword = new BCryptPasswordEncoder().encode(testemp.getPassword());
        UserDetails user = new User(testemp.getUsername(), encyptPassword,
                new ArrayList<>());
        token = jwtTokenUtil.generateToken(user);
        when(employeeRepository.findByUsername("test")).thenReturn(Optional.of(testemp));
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetEmployees() throws Exception {
        Employee testemp2 = new Employee("test2", "test2", "test2");

        List<Employee> allEmployees = Arrays.asList(testemp, testemp2);
        when(employeeRepository.findAll()).thenReturn(allEmployees);

        mvc.perform(
                MockMvcRequestBuilders.get("/employees")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("test"))
                .andExpect(jsonPath("$[*].id").exists());
    }

    @Test
    public void test_Retrieving_One_Employee() throws Exception {
        long id = 1;
        // given
        when(employeeRepository.findById(id)).thenReturn(Optional.of(testemp));

        mvc.perform(MockMvcRequestBuilders
                .get("/employees/{id}", id)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testemp.getId()));
    }

    @Test
    public void test_Saving_One_Employee() throws Exception {
        // given
        Employee testemp2 = new Employee("test2", "test2", "test2");
        when(employeeRepository.save(testemp2)).thenReturn(testemp2);
        String body = asJsonString(testemp2);
        mvc.perform(MockMvcRequestBuilders
                .post("/employees")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    public void test_modifying_One_Employee() throws Exception {
        // given
        Employee testemp2 = new Employee("test2", "test2", "test2");
        testemp2.setId(2l);
        when(employeeRepository.save(testemp2)).thenReturn(testemp2);
        String body = asJsonString(testemp2);
        mvc.perform(MockMvcRequestBuilders
                .put("/employees/{id}",testemp2.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void test_Deleting_One_Employee() throws Exception {
        // given
        Employee testemp2 = new Employee("test2", "test2", "test2");
        testemp2.setId(2l);
        when(employeeRepository.save(testemp2)).thenReturn(testemp2);
        mvc.perform(MockMvcRequestBuilders
                .delete("/employees/{id}",testemp2.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());
    }
}