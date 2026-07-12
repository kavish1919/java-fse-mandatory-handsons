package com.cognizant.loan.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.loan.model.Loan;

/**
 * REST Controller for Loan Microservice handling GET /loans/{number}.
 */
@RestController
public class LoanController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoanController.class);

    /**
     * Returns loan details for a given loan number without backend connectivity.
     * 
     * @param number Loan account number from path parameter
     * @return Sample dummy Loan object as specified in the exercise
     */
    @GetMapping("/loans/{number}")
    public Loan getLoan(@PathVariable String number) {
        LOGGER.info("Start getLoan endpoint with number: {}", number);
        Loan loan = new Loan(number, "car", 400000, 3258, 18);
        LOGGER.info("End getLoan endpoint");
        return loan;
    }
}
