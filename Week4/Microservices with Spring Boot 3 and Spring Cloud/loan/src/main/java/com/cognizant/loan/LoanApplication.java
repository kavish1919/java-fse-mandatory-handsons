package com.cognizant.loan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Bootstrap class for Loan Microservice (`loan`).
 */
@SpringBootApplication
public class LoanApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoanApplication.class);

    public static void main(String[] args) {
        LOGGER.info("Start LoanApplication on configured port 8081");
        SpringApplication.run(LoanApplication.class, args);
        LOGGER.info("End LoanApplication");
    }
}
