package com.cognizant.jwtlearn.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * AuthenticationController handling GET /authenticate requests.
 * Reads Authorization header, decodes credentials, and generates a JWT token.
 */
@RestController
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    private static final String SECRET_KEY = "secretkey";

    /**
     * Authenticates the user and returns a JSON response containing the generated JWT token.
     * 
     * @param authHeader HTTP Authorization header (`Authorization: Basic dXNlcjpwd2Q=`)
     * @return JSON response map {"token": "<jwt_string>"}
     */
    @GetMapping("/authenticate")
    public Map<String, String> authenticate(@RequestHeader("Authorization") String authHeader) {
        LOGGER.info("Start authenticate endpoint in AuthenticationController");
        LOGGER.debug("Received Authorization Header: {}", authHeader);

        // Step 2: Read Authorization header and decode username and password
        String user = getUser(authHeader);
        LOGGER.debug("Decoded User from Authorization Header: {}", user);

        // Step 3: Generate token based on the user retrieved
        String token = generateJwt(user);
        LOGGER.debug("Generated JWT Token: {}", token);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        LOGGER.info("End authenticate endpoint in AuthenticationController");
        return response;
    }

    /**
     * Helper method to decode Base64 encoded Basic Authentication credentials.
     * Extracts username from "username:password".
     */
    private String getUser(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length()).trim();
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(decodedBytes, StandardCharsets.UTF_8);
            return credentials.split(":", 2)[0];
        }
        return "unknown";
    }

    /**
     * Helper method using JJWT to build and sign the JSON Web Token.
     */
    private String generateJwt(String user) {
        JwtBuilder builder = Jwts.builder();
        builder.setSubject(user);
        builder.setIssuedAt(new Date());
        builder.setExpiration(new Date((new Date()).getTime() + 1200000)); // 20 minutes validity
        builder.signWith(SignatureAlgorithm.HS256, SECRET_KEY);
        return builder.compact();
    }
}
