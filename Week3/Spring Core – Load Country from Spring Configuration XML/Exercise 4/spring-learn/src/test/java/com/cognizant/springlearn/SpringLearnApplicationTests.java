package com.cognizant.springlearn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Unit Test verifying Spring XML configuration loading and Country bean properties.
 */
public class SpringLearnApplicationTests {

    @Test
    public void testCountryBeanLoading() {
        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        Country country = context.getBean("country", Country.class);
        
        assertNotNull(country, "Country bean should be retrieved from IoC container");
        assertEquals("IN", country.getCode(), "Country code should match XML configuration value");
        assertEquals("India", country.getName(), "Country name should match XML configuration value");
    }
}
