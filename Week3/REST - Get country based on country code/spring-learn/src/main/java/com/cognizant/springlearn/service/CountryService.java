package com.cognizant.springlearn.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.cognizant.springlearn.model.Country;

/**
 * Service class for Country operations.
 * Loads country list from country.xml and performs case-insensitive matching.
 */
@Service
public class CountryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryService.class);

    /**
     * Retrieves a Country by its two-letter ISO code (case-insensitive).
     * 
     * @param code Two-character ISO country code (e.g., "in", "us")
     * @return Matching Country object or null if not found
     */
    public Country getCountry(String code) {
        LOGGER.info("Start getCountry in CountryService with code: {}", code);
        
        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        @SuppressWarnings("unchecked")
        List<Country> countryList = (List<Country>) context.getBean("countryList");

        // Case-insensitive matching using Java 8 Lambda / Stream API
        Country matchingCountry = countryList.stream()
                .filter(country -> country.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);

        LOGGER.debug("Found country: {}", matchingCountry);
        LOGGER.info("End getCountry in CountryService");
        return matchingCountry;
    }
}
