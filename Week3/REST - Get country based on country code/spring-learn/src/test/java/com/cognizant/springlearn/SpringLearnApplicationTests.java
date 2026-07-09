package com.cognizant.springlearn;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration Test verifying GET /country/{code} and /countries/{code} endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SpringLearnApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetCountryByCodeIn() throws Exception {
        mockMvc.perform(get("/country/in"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code").value("IN"))
               .andExpect(jsonPath("$.name").value("India"));
    }

    @Test
    public void testGetCountriesByCodeUs() throws Exception {
        mockMvc.perform(get("/countries/us"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.code").value("US"))
               .andExpect(jsonPath("$.name").value("United States"));
    }
}
