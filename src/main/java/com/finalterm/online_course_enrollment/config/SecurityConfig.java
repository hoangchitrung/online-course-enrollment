package com.finalterm.online_course_enrollment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(crsf -> crsf.disable())
                .authorizeHttpRequests(
                        auth -> auth
                                // Public
                                .requestMatchers("/", "/signin", "/signup", "/login", "/register", "/error",
                                        "/favicon.ico")
                                .permitAll()
                                // Public API - Courses
                                .requestMatchers("/api/courses", "/api/courses/**").permitAll()
                                // User + Admin
                                .requestMatchers("/user/**", "/cart/**", "/order/**", "/payment/**", "/learning/**")
                                .hasAnyRole("USER", "ADMIN")
                                // Admin
                                .requestMatchers("/api/admin/**", "/admin/**").hasAnyRole("ADMIN")
                                // the rest will the authenticated
                                .requestMatchers("/api/**").authenticated()
                                .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/signin")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/user/dashboard", true))
                .logout(logout -> logout
                        .logoutSuccessUrl("/"))
                .httpBasic(httpBasic -> httpBasic.disable()); // prevent to popup basic auth
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
