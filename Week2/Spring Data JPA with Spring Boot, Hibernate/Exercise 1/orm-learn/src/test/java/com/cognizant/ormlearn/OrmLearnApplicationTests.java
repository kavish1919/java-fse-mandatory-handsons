package com.cognizant.ormlearn;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cognizant.ormlearn.model.Country;
import com.cognizant.ormlearn.service.CountryService;

/**
 * Unit / Integration Test Class demonstrating testing inside src/test/java.
 * 
 * SME Walkthrough Notes:
 * - @SpringBootTest: Bootstraps the full Spring application context for testing.
 * - Tests can autowire services/repositories directly and assert expected behaviors.
 */
@SpringBootTest
public class OrmLearnApplicationTests {

    @Autowired
    private CountryService countryService;

    @Test
    public void contextLoads() {
        assertNotNull(countryService, "CountryService should be loaded into application context");
    }

    @Test
    public void testGetAllCountriesNotNull() {
        List<Country> countries = countryService.getAllCountries();
        assertNotNull(countries, "Country list should not be null");
        assertTrue(countries.size() >= 0, "Country list size should be non-negative");
    }
}
