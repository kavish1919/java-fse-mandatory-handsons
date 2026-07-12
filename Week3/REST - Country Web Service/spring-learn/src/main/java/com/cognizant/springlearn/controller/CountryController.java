package com.cognizant.springlearn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.springlearn.model.Country;
import com.cognizant.springlearn.service.CountryService;

/**
 * CountryController handling REST endpoints for Country resources.
 */
@RestController
public class CountryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryController.class);

    @Autowired
    private CountryService countryService;

    /**
     * REST endpoint returning India country details loaded from country.xml configuration.
     * Mapped to /country as specified by the REST - Country Web Service exercise.
     * 
     * @return India Country object serialized automatically into JSON
     */
    @RequestMapping(value = "/country", method = RequestMethod.GET)
    public Country getCountryIndia() {
        LOGGER.info("Start getCountryIndia");
        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        Country country = context.getBean("in", Country.class);
        LOGGER.info("End getCountryIndia");
        return country;
    }

    /**
     * Retrieves a specific country by its two-letter ISO code (case-insensitive).
     * Mapped to /countries/{code} and /country/{code}.
     * 
     * @param code Two-character ISO code extracted via @PathVariable
     * @return Matching Country object or null if not found
     */
    @GetMapping({"/countries/{code}", "/country/{code}"})
    public Country getCountry(@PathVariable String code) {
        LOGGER.info("Start getCountry endpoint in CountryController with code: {}", code);
        Country country = countryService.getCountry(code);
        LOGGER.info("End getCountry endpoint in CountryController");
        return country;
    }
}
