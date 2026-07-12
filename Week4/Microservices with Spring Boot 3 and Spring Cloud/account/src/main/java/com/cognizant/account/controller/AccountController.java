package com.cognizant.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.account.model.Account;

/**
 * REST Controller for Account Microservice handling GET /accounts/{number}.
 */
@RestController
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    /**
     * Returns account details for a given account number without backend connectivity.
     * 
     * @param number Account number from path parameter
     * @return Sample dummy Account object as specified in the exercise
     */
    @GetMapping("/accounts/{number}")
    public Account getAccount(@PathVariable String number) {
        LOGGER.info("Start getAccount endpoint with number: {}", number);
        Account account = new Account(number, "savings", 234343);
        LOGGER.info("End getAccount endpoint");
        return account;
    }
}
