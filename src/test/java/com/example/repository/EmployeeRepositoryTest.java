package com.example.repository;

import com.example.domain.Employee;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
//import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class EmployeeRepositoryTest {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByUsername() {
        // given
        Employee emp = new Employee("test","test","test");
        entityManager.persist(emp);
        entityManager.flush();

        // when
        Employee emp2 = employeeRepository.findByUsername(emp.getUsername()).get();

        // then
        //assertThat(emp2.getUsername()).isEqualTo(emp.getUsername());
        Assert.assertEquals(emp2.getUsername(),emp.getUsername());
    }
}

