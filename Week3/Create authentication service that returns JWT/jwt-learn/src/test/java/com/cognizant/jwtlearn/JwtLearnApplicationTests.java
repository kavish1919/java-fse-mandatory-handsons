package com.cognizant.jwtlearn;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Unit / Integration Test verifying GET /authenticate endpoint using HTTP Basic authentication.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class JwtLearnApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAuthenticateEndpointSuccess() throws Exception {
        mockMvc.perform(get("/authenticate").with(httpBasic("user", "pwd")))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    public void testAuthenticateEndpointUnauthorized() throws Exception {
        mockMvc.perform(get("/authenticate").with(httpBasic("user", "wrongpassword")))
               .andExpect(status().isUnauthorized());
    }
}
