package com.tripscanner.TripScanner.security.webSecurity;

import com.tripscanner.TripScanner.service.RepositoryUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    RepositoryUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Public pages
        http.authorizeRequests().antMatchers("/").permitAll();
        http.authorizeRequests().antMatchers("/login").permitAll();
        http.authorizeRequests().antMatchers("/sign").permitAll();
        http.authorizeRequests().antMatchers("/details/**").permitAll();
        http.authorizeRequests().antMatchers("/destination/*/image").permitAll();
        http.authorizeRequests().antMatchers("/itinerary/*/image").permitAll();
        http.authorizeRequests().antMatchers("/place/*/image").permitAll();
        http.authorizeRequests().antMatchers("/user/*/image").permitAll();

        // Private pages
        http.authorizeRequests().antMatchers("/profile").hasAnyRole("USER");
        http.authorizeRequests().antMatchers("/profile/edit").hasAnyRole("USER");
        http.authorizeRequests().antMatchers("/itinerary/add/place/**").hasAnyRole("USER");
        http.authorizeRequests().antMatchers("/myItineraries/**").hasAnyRole("USER");
        http.authorizeRequests().antMatchers("/export/**").hasAnyRole("USER");
        http.authorizeRequests().antMatchers("/reviews/add/*").hasAnyRole("USER");
        http.authorizeRequests().antMatchers("/management/**").hasAnyRole("ADMIN");
        http.authorizeRequests().antMatchers("/place/*/delete").hasAnyRole("USER");
        // LEFT TO ADD MORE WHEN MERGE TO DEV

        // Login form
        http.formLogin().loginPage("/login");
        http.formLogin().usernameParameter("username");
        http.formLogin().passwordParameter("password");
        http.formLogin().defaultSuccessUrl("/");
        http.formLogin().failureUrl("/login");

        // Logout
        http.logout().logoutUrl("/logout");
        http.logout().logoutSuccessUrl("/");

        // Allow H2 console
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
        http.headers().frameOptions().sameOrigin();

    }

}