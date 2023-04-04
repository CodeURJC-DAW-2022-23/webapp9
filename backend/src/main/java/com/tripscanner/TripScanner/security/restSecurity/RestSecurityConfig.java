package com.tripscanner.TripScanner.security.restSecurity;

import com.tripscanner.TripScanner.security.jwt.JwtRequestFilter;
import com.tripscanner.TripScanner.service.RepositoryUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Order(1)
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    RepositoryUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    //Expose AuthenticationManager as a Bean to be used in other services
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**");

        // URLs that need authentication to access to it
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/users/me").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/users/me").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/users/me/image").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/users/me/image").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/users/me/itineraries").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/itineraries/*/export").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/itineraries/").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/itineraries/*/copy").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/itineraries/**").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/itineraries/**").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/management/**").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/management/**").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/management/**").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/management/**").hasRole("ADMIN");

        // Other URLs can be accessed without authentication
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/itineraries/").permitAll();

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