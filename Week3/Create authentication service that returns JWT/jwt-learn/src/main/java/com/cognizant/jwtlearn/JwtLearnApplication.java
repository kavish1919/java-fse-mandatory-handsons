package com.cognizant.jwtlearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Bootstrap class for JWT Authentication Service (`jwt-learn`).
 */
@SpringBootApplication
public class JwtLearnApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtLearnApplication.class);

    public static void main(String[] args) {
        LOGGER.info("Start");
        SpringApplication.run(JwtLearnApplication.class, args);
        LOGGER.info("Inside main");
        LOGGER.info("End");
    }
}
