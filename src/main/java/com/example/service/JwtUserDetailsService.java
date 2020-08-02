package com.example.service;

import com.example.domain.Employee;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws ResourceNotFoundException {
        Employee user = employeeRepository.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("Employyee","username",username));

        return new User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }

}