package com.tripscanner.TripScanner.security.restSecurity;

import com.tripscanner.TripScanner.security.jwt.JwtRequestFilter;
import com.tripscanner.TripScanner.service.RepositoryUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;

import java.security.SecureRandom;

@Configuration
@Order(2)
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public RepositoryUserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    // URLs that need authentication to access to it
    // Other URLs can be accessed without authentication

    // Disable CSRF protection (it is difficult to implement in REST APIs)
            http.csrf().disable();

    // Disable Http Basic Authentication
            http.httpBasic().disable();

    // Disable Form login Authentication
            http.formLogin().disable();

    // Avoid creating session
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // Add JWT Token filter
            http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
