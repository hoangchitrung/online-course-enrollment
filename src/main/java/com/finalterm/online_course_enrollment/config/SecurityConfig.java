package com.finalterm.online_course_enrollment.config;

import com.finalterm.online_course_enrollment.security.JwtAuthenticationFilter;
import com.finalterm.online_course_enrollment.services.CustomerUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomerUserDetailsService customerUserDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomerUserDetailsService customerUserDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customerUserDetailsService = customerUserDetailsService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(crsf -> crsf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth -> auth
                                // Public
                                .requestMatchers("/", "/signin", "/signup", "/login", "/register", "/error",
                                        "/favicon.ico", "/courses",
                                        "/css/**", "/js/**", "/images/**", "/webjars/**")
                                .permitAll()
                                // Public pages and API - Courses and auth
                                .requestMatchers(HttpMethod.POST, "/api/courses").hasRole("ADMIN")
                                .requestMatchers("/course/**", "/api/courses", "/api/courses/**", "/api/auth/**")
                                .permitAll()
                                // User + Admin
                                .requestMatchers("/user/**", "/cart/**", "/order/**", "/payment/**", "/learning/**")
                                .hasAnyRole("USER", "ADMIN")
                                // Admin
                                .requestMatchers("/api/admin/**", "/admin/**").hasAnyRole("ADMIN")
                                // the rest will the authenticated
                                .requestMatchers("/api/**").authenticated()
                                .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (request.getRequestURI().startsWith("/api/")) {
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                            } else {
                                response.sendRedirect("/signin");
                            }
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            if (request.getRequestURI().startsWith("/api/")) {
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                            } else {
                                response.sendRedirect("/signin");
                            }
                        }))
                .formLogin(form -> form
                        .loginPage("/signin")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .successHandler((request, response, authentication) -> {
                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
                            if (isAdmin) {
                                response.sendRedirect("/admin/dashboard");
                            } else {
                                response.sendRedirect("/user/dashboard");
                            }
                        })
                        .permitAll())
                .rememberMe(remember -> remember
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(7 * 24 * 60 * 60)
                        .key("remember-me-key")
                        .userDetailsService(customerUserDetailsService))
                .logout(logout -> logout
                        .logoutSuccessUrl("/"))
                .httpBasic(httpBasic -> httpBasic.disable())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.security.authentication.AuthenticationManager authenticationManager(
            org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
