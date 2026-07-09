package com.cognizant.springlearn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController for Hello World RESTful Web Service.
 * Handles HTTP GET requests to "/hello" and returns a hardcoded string.
 */
@RestController
public class HelloController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    /**
     * REST Endpoint returning "Hello World!!".
     * Includes start and end debug logs as required.
     * 
     * @return Hardcoded string "Hello World!!"
     */
    @GetMapping("/hello")
    public String sayHello() {
        LOGGER.info("Start");
        String response = "Hello World!!";
        LOGGER.info("End");
        return response;
    }
}
