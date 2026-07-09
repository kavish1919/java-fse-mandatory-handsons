package com.cognizant.springlearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Country POJO configured via Spring XML (`country.xml`).
 * Contains instance variables code and name, and logs all constructor and setter/getter invocations.
 */
public class Country {

    private static final Logger LOGGER = LoggerFactory.getLogger(Country.class);

    private String code;
    private String name;

    /**
     * Empty parameter constructor required by Spring IoC container during bean instantiation.
     */
    public Country() {
        LOGGER.debug("Inside Country Constructor.");
    }

    public String getCode() {
        LOGGER.debug("Inside getCode.");
        return code;
    }

    /**
     * Setter method invoked during XML <property name="code" value="IN" /> injection.
     */
    public void setCode(String code) {
        LOGGER.debug("Inside setCode.");
        this.code = code;
    }

    public String getName() {
        LOGGER.debug("Inside getName.");
        return name;
    }

    /**
     * Setter method invoked during XML <property name="name" value="India" /> injection.
     */
    public void setName(String name) {
        LOGGER.debug("Inside setName.");
        this.name = name;
    }

    @Override
    public String toString() {
        return "Country [code=" + code + ", name=" + name + "]";
    }
}
