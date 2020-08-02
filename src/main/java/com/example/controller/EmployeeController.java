package com.example.controller;

import com.example.domain.Employee;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class EmployeeController {

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @GetMapping("/employees")
    public List<Employee> all() {
        return repository.findAll();
    }

    @PostMapping(value = "/employees")
    public ResponseEntity<Employee> newEmployee(@RequestBody Employee newEmployee) {
        newEmployee.setPassword(bcryptEncoder.encode(newEmployee.getPassword()));
        return new ResponseEntity<Employee>(repository.save(newEmployee), HttpStatus.CREATED);
    }

    // Single item

    @GetMapping("/employees/{id}")
    public Employee findById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employyee","id",id));
    }

    @PutMapping(value = "/employees/{id}")
    public ResponseEntity<Employee> update(@RequestBody Employee newEmployee, @PathVariable Long id) {
        Employee employee = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employyee","id",id));
        newEmployee.setId(id);
        newEmployee.setPassword(bcryptEncoder.encode(newEmployee.getPassword()));// encode password
        return new ResponseEntity<Employee>(repository.save(newEmployee), HttpStatus.OK);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
    }
}
