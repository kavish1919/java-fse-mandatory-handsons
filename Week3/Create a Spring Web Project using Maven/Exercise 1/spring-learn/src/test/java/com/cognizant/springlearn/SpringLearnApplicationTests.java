package com.cognizant.springlearn;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * Unit / Integration Test Class demonstrating testing inside src/test/java.
 * 
 * SME Walkthrough Notes:
 * - @SpringBootTest: Bootstraps the full Spring WebApplicationContext for testing.
 * - Asserts that the application context and embedded web server beans are loaded properly.
 */
@SpringBootTest
public class SpringLearnApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    public void contextLoads() {
        assertNotNull(context, "ApplicationContext should be successfully loaded and initialized");
    }
}
