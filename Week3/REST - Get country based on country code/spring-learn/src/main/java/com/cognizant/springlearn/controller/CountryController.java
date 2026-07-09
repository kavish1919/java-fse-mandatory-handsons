package com.cognizant.springlearn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.springlearn.model.Country;
import com.cognizant.springlearn.service.CountryService;

/**
 * CountryController handling REST requests for Country resources.
 */
@RestController
public class CountryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryController.class);

    @Autowired
    private CountryService countryService;

    /**
     * Retrieves a specific country by its code (case-insensitive).
     * Mapped to both /countries/{code} and /country/{code} to satisfy exercise specifications.
     * 
     * @param code Two-character ISO country code extracted via @PathVariable
     * @return Matching Country object serialized as JSON
     */
    @GetMapping({"/countries/{code}", "/country/{code}"})
    public Country getCountry(@PathVariable String code) {
        LOGGER.info("Start getCountry endpoint in CountryController with code: {}", code);
        Country country = countryService.getCountry(code);
        LOGGER.info("End getCountry endpoint in CountryController");
        return country;
    }
}
