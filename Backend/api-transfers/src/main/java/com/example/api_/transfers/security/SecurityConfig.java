package com.example.api_.transfers.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)// Desactivar CSRF
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/v3/api-docs/**",  // Documentación de OpenAPI
                                "/swagger-ui/**",   // Recursos de Swagger UI
                                "/swagger-ui.html"  // Página principal de Swagger
                        ).permitAll()
                        .requestMatchers("/accounts/create/**").permitAll()
                        .requestMatchers("/accounts/update/alias/**").permitAll()
                        .requestMatchers("/accounts/{accountId}/transferences/**").permitAll()// Permitir acceso sin autenticación a ciertos endpoints
                        .anyRequest().authenticated() // Requiere autenticación para el resto
                )
                //.addFilterBefore(jwtAuthenticationFilter(), BasicAuthenticationFilter.class); // Agregar el filtro de autenticación JWT
                .sessionManagement(sessionManager->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class);
        return http.build();
    }
}
