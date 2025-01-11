package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Read values from application.properties
    @Value("${security.admin.user.name}")
    private String adminUsername;
    @Value("${security.admin.password}")
    private String adminPassword;
    @Value("${security.user.user.name}")
    private String userUsername;
    @Value("${security.user.password}")
    private String userPassword;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF & CORS
        // Only admin can access the cache endpoint.
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(req -> req
                        .requestMatchers(
                                new AntPathRequestMatcher("/test-admin"),
                                new AntPathRequestMatcher("/actuator/caches/"),
                                new AntPathRequestMatcher("/cache/**"),
                                new AntPathRequestMatcher("/actuator/metrics")
                        )
                        .hasRole("ADMIN")
                        .anyRequest()
                        .authenticated())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withUsername(adminUsername)
                .password(passwordEncoder().encode(adminPassword))
                .roles("ADMIN") // Assign ADMIN role
                .build();

        UserDetails user = User.withUsername(userUsername)
                .password(passwordEncoder().encode(userPassword))
                .roles("USER") // Assign USER role
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    // A method to encrypt our password.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
