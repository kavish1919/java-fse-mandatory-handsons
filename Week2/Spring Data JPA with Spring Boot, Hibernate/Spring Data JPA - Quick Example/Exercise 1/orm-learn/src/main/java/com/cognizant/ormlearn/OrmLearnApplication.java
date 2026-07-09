package com.cognizant.ormlearn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.cognizant.ormlearn.model.Country;
import com.cognizant.ormlearn.service.CountryService;

/**
 * Main Application class for Spring Boot ORM Learn Demo.
 * 
 * SME Walkthrough Notes:
 * 1. @SpringBootApplication: This is a convenience annotation that combines three key annotations:
 *    - @Configuration: Tags the class as a source of bean definitions for the application context.
 *    - @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings, other beans,
 *      and various property settings (e.g., automatically configuring Spring Data JPA and DataSource).
 *    - @ComponentScan: Tells Spring to look for other components, configurations, and services in the 'com.cognizant.ormlearn'
 *      package and its sub-packages.
 * 
 * 2. main() Method Walkthrough:
 *    - Serves as the entry point of the Java application.
 *    - SpringApplication.run() bootstraps the Spring Boot application, initializes the embedded or standalone IoC container
 *      (ApplicationContext), registers all beans, and executes autoconfiguration.
 *    - Retrieves the CountryService bean from the initialized ApplicationContext using context.getBean(CountryService.class).
 *    - Invokes testGetAllCountries() which uses CountryService to query the database and log retrieved records.
 */
@SpringBootApplication
public class OrmLearnApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrmLearnApplication.class);

    private static CountryService countryService;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(OrmLearnApplication.class, args);
        LOGGER.info("Inside main");

        countryService = context.getBean(CountryService.class);
        testGetAllCountries();
    }

    private static void testGetAllCountries() {
        LOGGER.info("Start");
        List<Country> countries = countryService.getAllCountries();
        LOGGER.debug("countries={}", countries);
        LOGGER.info("End");
    }
}
