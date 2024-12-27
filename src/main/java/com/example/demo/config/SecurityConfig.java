package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class SecurityConfig {

    // Read values from application.properties
    @Value("${security.admin.user.name}")
    private String adminUsername;
    @Value("${security.admin.password}")
    private String adminPassword;
    @Value("${security.user.admin.role}")
    private String adminRole;
    @Value("${security.user.user.name}")
    private String userUsername;
    @Value("${security.user.password}")
    private String userPassword;
    @Value("${security.user.user.role}")
    private String userRole;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Disable CSRF for simplicity (not recommended for production)
                .authorizeHttpRequests(auth -> auth
                        .antMatchers("/cache/stats").hasRole("ADMIN") // Only user with the ADMIN role can access the cache endpoint.
                        .anyRequest()
                        .authenticated() // All other endpoints require authentication
                )
                .httpBasic(); // Enable basic authentication for simplicity
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withUsername(adminUsername)
                .password(passwordEncoder().encode(adminPassword))
                .roles("ADMIN") // Assign ADMIN role
                .build();

        //
        UserDetails user = User.withUsername(userUsername)
                .password(passwordEncoder().encode(userPassword))
                .roles("USER") // Assign USER role
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
