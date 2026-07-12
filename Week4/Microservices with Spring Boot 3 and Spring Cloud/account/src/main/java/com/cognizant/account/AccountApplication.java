package com.cognizant.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Bootstrap class for Account Microservice (`account`).
 */
@SpringBootApplication
public class AccountApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountApplication.class);

    public static void main(String[] args) {
        LOGGER.info("Start AccountApplication on default port 8080");
        SpringApplication.run(AccountApplication.class, args);
        LOGGER.info("End AccountApplication");
    }
}
