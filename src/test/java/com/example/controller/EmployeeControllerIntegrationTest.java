package com.example.controller;

import com.example.domain.Employee;
import com.example.domain.JwtRequest;
import com.example.domain.JwtResponse;
import com.example.repository.EmployeeRepository;
import com.example.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    ObjectMapper objectMapper;


    HttpHeaders headers;

    String token = null;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setUp() throws Exception {
       /* testemp = new Employee("test", "test", "test");
        String encypassword = new BCryptPasswordEncoder().encode(testemp.getPassword());
        testemp.setPassword(encypassword);// encrypt password
        employeeRepository.save(testemp);*/

       /* HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setUsername("test");
        jwtRequest.setUsername("test");
        String requestJson = asJsonString(jwtRequest);
        //System.out.println(requestJson);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
        String result = restTemplate.postForObject("/authenticate", entity, String.class);
        System.out.println(result);*/

        Employee testemp = employeeRepository.findByUsername("test").get();
        String encyptPassword = new BCryptPasswordEncoder().encode(testemp.getPassword());
        UserDetails user = new User(testemp.getUsername(), encyptPassword,
                new ArrayList<>());
        token = jwtTokenUtil.generateToken(user);

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+token);

        //System.out.println(token);
    }

    @Test
    public void testGetEmployees() {
        String requestJson = "";
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);

        ResponseEntity<Employee[]> result= restTemplate.exchange("/employees", HttpMethod.GET,entity, Employee[].class);
        Assert.assertEquals(result.getBody().length,1);
    }

    @Test
    public void test_Retrieving_One_Employee() throws Exception {
        Employee testemp2 = new Employee("test2", "test2", "test2");
        employeeRepository.save(testemp2);

        String requestJson = "";
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);

        ResponseEntity<Employee> result= restTemplate.exchange("/employees/"+testemp2.getId(), HttpMethod.GET,entity,Employee.class);
        Assert.assertEquals(result.getBody().getUsername(),testemp2.getUsername());
    }

    @Test
    public void test_Saving_One_Employee() throws Exception {
        Employee testemp3 = new Employee("test3", "test3", "test3");
        String requestJson = asJsonString(testemp3);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);

        ResponseEntity<Employee> result= restTemplate.exchange("/employees", HttpMethod.POST,entity,Employee.class);
        Assert.assertEquals(result.getBody().getUsername(),testemp3.getUsername());
    }

    @Test
    public void test_modifying_One_Employee() throws Exception {
        Employee testemp4 = new Employee("test4", "test4", "test4");
        employeeRepository.save(testemp4);

        String requestJson = asJsonString(testemp4);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);

        ResponseEntity<Employee> result= restTemplate.exchange("/employees/"+testemp4.getId(), HttpMethod.PUT,entity,Employee.class);
        Assert.assertEquals(result.getBody().getUsername(),testemp4.getUsername());
    }
    @Test
    public void test_Deleting_One_Employee() throws Exception {
        Employee testemp5 = new Employee("test5", "test5", "test5");
        employeeRepository.save(testemp5);

        String requestJson = asJsonString(testemp5);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);

        ResponseEntity<String> result= restTemplate.exchange("/employees/"+testemp5.getId(), HttpMethod.DELETE,entity,String.class);
        //Assert.assertEquals(result.getStatusCode(),testemp2.getUsername());
        System.out.println(result.getStatusCode());
        System.out.println(result.getStatusCodeValue());
    }
}