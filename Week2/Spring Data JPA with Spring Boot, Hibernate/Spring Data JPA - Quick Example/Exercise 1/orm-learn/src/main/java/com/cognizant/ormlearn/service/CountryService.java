package com.cognizant.ormlearn.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.ormlearn.model.Country;
import com.cognizant.ormlearn.repository.CountryRepository;

/**
 * Service Class for encapsulating business logic relating to Country entity.
 * 
 * Notes on Service Layer and Annotations:
 * - @Service: Stereotype annotation marking this class as a Spring business service bean in IoC container.
 * - @Autowired: Automatically injects the CountryRepository bean dependency into this service.
 * - @Transactional: Ensures the method execution occurs within a transactional boundary managed by Spring transaction manager.
 */
@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    /**
     * Retrieves all countries from the database.
     * 
     * @return List of all Country entities.
     */
    @Transactional
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }
}
