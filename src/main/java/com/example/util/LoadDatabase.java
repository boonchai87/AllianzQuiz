package com.example.util;

import com.example.DemoApplication;
import com.example.domain.Employee;
import com.example.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {
        return args -> {
            String password = new BCryptPasswordEncoder().encode("test");
            log.info("Preloading " + repository.save(new Employee("mr. Test", "test" ,password)));
        };
    }
}