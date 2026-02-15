package com.defaultPackage.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.defaultPackage.service.JwtService.JwtFilter;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class Security {
	
	
	@Autowired
	JwtFilter jwtFilter;

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	    http
	        .csrf(csrf -> csrf.disable())
	        .sessionManagement(session ->
	        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    )
	        .authorizeHttpRequests(auth -> auth
	        		.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
	                // Public routes
	                .requestMatchers(
	                    "/",
	                    "/chat.html",
	                    "/signup.html",
	                    "/login.html",
	                    "/user/manage/**",
	                    "/css/**",
	                    "/js/**"
	                ).permitAll()

	                // Everything else secured
	                .anyRequest().authenticated()
	            )
	        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
