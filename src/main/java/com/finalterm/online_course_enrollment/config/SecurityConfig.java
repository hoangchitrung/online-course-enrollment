package com.finalterm.online_course_enrollment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.finalterm.online_course_enrollment.services.CustomerUserDetailsService;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(crsf -> crsf.disable())
                .authorizeHttpRequests(
                        auth -> auth
                                // Public
                                .requestMatchers("/", "/login", "/register", "/error", "/favicon.ico").permitAll()
                                // User + Admin
                                .requestMatchers("/user/**", "/cart/**", "/order/**", "/payment/**", "/learning/**")
                                .hasAnyRole("USER", "ADMIN")
                                // Admin
                                .requestMatchers("/api/admin/**", "/admin/**").hasAnyRole("ADMIN")
                                // the rest will the authenticated
                                .requestMatchers("/api/**").authenticated()
                                .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults()) // use the default login form
                .logout(Customizer.withDefaults())
                .httpBasic(httpBasic -> httpBasic.disable()); // prevent to popup basic auth
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider atuehAuthenticationProvider(CustomerUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
