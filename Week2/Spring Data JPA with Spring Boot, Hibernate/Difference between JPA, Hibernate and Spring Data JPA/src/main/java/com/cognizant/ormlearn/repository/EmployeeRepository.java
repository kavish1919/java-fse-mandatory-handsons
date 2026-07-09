package com.cognizant.ormlearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognizant.ormlearn.model.Employee;

/**
 * Spring Data JPA Repository interface.
 * Notice how zero implementation code is written!
 * JpaRepository provides out-of-the-box methods like save(), findById(), findAll(), etc.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
