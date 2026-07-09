package com.cognizant.ormlearn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cognizant.ormlearn.model.Employee;
import com.cognizant.ormlearn.repository.EmployeeRepository;

/**
 * Service Class demonstrating clean Spring Data JPA + Declarative Transaction management.
 * Notice how all try-catch-rollback-finally session handling is eliminated!
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Persists an Employee entity cleanly using Spring Data JPA.
     * @Transactional ensures that Spring intercepts this method, automatically
     * opening/closing sessions and handling commits or rollbacks on error.
     */
    @Transactional
    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }
}
