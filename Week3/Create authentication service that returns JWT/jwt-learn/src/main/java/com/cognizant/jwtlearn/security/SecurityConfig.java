package com.cognizant.jwtlearn.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security Configuration (`SecurityConfig`).
 * Configures HTTP Basic authentication for the /authenticate endpoint and provides an in-memory user store (`user:pwd`).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * In-Memory UserDetailsManager providing the user credentials (`user:pwd`) tested via curl -u option.
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        LOGGER.info("Start userDetailsService in SecurityConfig");
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("pwd")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    /**
     * Configures HTTP Basic authentication and secures the /authenticate endpoint.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        LOGGER.info("Start filterChain configuration in SecurityConfig");
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/authenticate").hasRole("USER")
            .anyRequest().authenticated()
            .and()
            .httpBasic();
        return http.build();
    }
}
