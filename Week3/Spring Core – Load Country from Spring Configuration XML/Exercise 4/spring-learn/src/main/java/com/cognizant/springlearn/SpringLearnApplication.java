package com.cognizant.springlearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main Application class for Spring Core XML Configuration Demo (`spring-learn`).
 * Demonstrates loading Spring IoC container (`ClassPathXmlApplicationContext`) and retrieving beans via `getBean()`.
 */
public class SpringLearnApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringLearnApplication.class);

    public static void main(String[] args) {
        LOGGER.info("Inside main - Start");
        displayCountry();
        LOGGER.info("Inside main - End");
    }

    /**
     * Reads the country bean from country.xml and displays details.
     */
    public static void displayCountry() {
        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        Country country = (Country) context.getBean("country", Country.class);
        LOGGER.debug("Country : {}", country.toString());
    }
}
