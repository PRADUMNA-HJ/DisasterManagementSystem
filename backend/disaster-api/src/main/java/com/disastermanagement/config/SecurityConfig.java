package com.disastermanagement.config;

import com.disastermanagement.security.CustomUserDetailsService;
import com.disastermanagement.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Spring Security 6 Configuration for stateless JWT Authentication and Role-Based Access Control.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter,
                          CustomUserDetailsService userDetailsService,
                          CorsConfigurationSource corsConfigurationSource) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // For H2 console
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"success\":false,\"message\":\"Unauthorized: " + authException.getMessage() + "\",\"data\":null,\"timestamp\":\"" + java.time.LocalDateTime.now() + "\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write("{\"success\":false,\"message\":\"Access Denied: You do not have permission for this resource\",\"data\":null,\"timestamp\":\"" + java.time.LocalDateTime.now() + "\"}");
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/v1/info").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // Public read-only endpoints for emergency information
                        .requestMatchers(HttpMethod.GET, "/api/v1/disasters/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/shelters/**").permitAll()

                        // User management (Admin only)
                        .requestMatchers("/api/v1/users/**").hasRole("ADMIN")

                        // Shelter creation/modification (Admin only)
                        .requestMatchers(HttpMethod.POST, "/api/v1/shelters/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/shelters/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/shelters/**").hasRole("ADMIN")

                        // Disaster management mutations
                        .requestMatchers(HttpMethod.PUT, "/api/v1/disasters/**").hasAnyRole("ADMIN", "RESCUE_TEAM")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/disasters/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/disasters/**").hasAnyRole("CITIZEN", "ADMIN", "VOLUNTEER", "RESCUE_TEAM")

                        // Resource request operations
                        .requestMatchers(HttpMethod.PUT, "/api/v1/resource-requests/*/status").hasAnyRole("ADMIN", "RESCUE_TEAM", "VOLUNTEER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/resource-requests/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/resource-requests/**").authenticated()

                        // Incident logs
                        .requestMatchers(HttpMethod.POST, "/api/v1/incident-logs/**").hasAnyRole("ADMIN", "RESCUE_TEAM", "VOLUNTEER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/incident-logs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/incident-logs/**").authenticated()

                        // All other API endpoints require authentication
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
