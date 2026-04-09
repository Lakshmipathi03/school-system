package com.example.school.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {

        UserDetails admin = User.withUsername("admin")
                .password("admin123")
                .roles("ADMIN")
                .build();

        UserDetails principal = User.withUsername("principal")
                .password("principal123")
                .roles("PRINCIPAL")
                .build();

        UserDetails teacher = User.withUsername("teacher")
                .password("teacher123")
                .roles("TEACHER")
                .build();

        UserDetails student = User.withUsername("student")
                .password("student123")
                .roles("STUDENT")
                .build();

        UserDetails parent = User.withUsername("parent")
                .password("parent123")
                .roles("PARENT")
                .build();

        return new InMemoryUserDetailsManager(admin, principal, teacher, student, parent);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth

                        // Public pages
                        .requestMatchers("/login", "/forgot-password", "/reset-password").permitAll()

                        // Role based access
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/principal/**").hasRole("PRINCIPAL")
                        .requestMatchers("/teacher/**").hasRole("TEACHER")
                        .requestMatchers("/student/**").hasRole("STUDENT")
                        .requestMatchers("/parent/**").hasRole("PARENT")

                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }

    @Bean
    @SuppressWarnings("deprecation")
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
}