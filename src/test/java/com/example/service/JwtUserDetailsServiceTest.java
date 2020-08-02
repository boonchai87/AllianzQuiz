package com.example.service;

import com.example.domain.Employee;
import com.example.repository.EmployeeRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
//import static org.assertj.core.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class JwtUserDetailsServiceTest {

    @InjectMocks
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    public void Successfully() throws Exception {
        //when
        Employee emp = new Employee("test","test","test");
        when(employeeRepository.findByUsername(emp.getUsername()))
                .thenReturn(Optional.of(emp));

        //Act
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(emp.getUsername());

        //Assert
        Assert.assertEquals(userDetails.getUsername(),emp.getUsername());
    }
}