package com.cognizant.springlearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Application bootstrap class for Spring Web Learn (`spring-learn`).
 * 
 * SME Walkthrough Notes:
 * 1. @SpringBootApplication combines:
 *    - @Configuration: Designates this class as a source of Spring bean definitions for application context.
 *    - @EnableAutoConfiguration: Instructs Spring Boot to automatically configure Spring Web MVC,
 *      DispatcherServlet, Jackson converters, and embedded Tomcat Server based on `spring-boot-starter-web`.
 *    - @ComponentScan: Automatically scans components across `com.cognizant.springlearn` and sub-packages.
 * 
 * 2. main() Method Walkthrough:
 *    - Entry point of the Java application.
 *    - SpringApplication.run() initializes the Spring WebApplicationContext and embedded Tomcat Server on port 8080.
 *    - LOGGER.info("Inside main") verifies that the main method is called and completes execution cleanly.
 */
@SpringBootApplication
public class SpringLearnApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringLearnApplication.class);

    public static void main(String[] args) {
        LOGGER.info("Start");
        SpringApplication.run(SpringLearnApplication.class, args);
        LOGGER.info("Inside main");
        LOGGER.info("End");
    }
}
