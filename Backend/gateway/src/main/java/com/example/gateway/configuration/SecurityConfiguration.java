package com.example.gateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
//@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configuración de CORS
                .authorizeExchange(exchange -> exchange
                        .anyExchange().permitAll() // Permitir todas las solicitudes
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable); // Deshabilitar CSRF

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name(), HttpMethod.PATCH.name()));
        configuration.setAllowedHeaders(List.of("*")); // Permitir todos los encabezados
        configuration.setAllowCredentials(true); // Permitir credenciales

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplicar a todas las rutas
        return source;
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        return new CorsWebFilter(corsConfigurationSource());
    }
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder, JwtGatewayFilter jwtGatewayFilter) {
        return builder.routes()
                .route("api_activity", r -> r.path("/accounts/{accountId}/activity/**")
                        .filters(f -> f.filter(jwtGatewayFilter))
                        .uri("http://localhost:9091")) // Ruta al microservicio api-activity
                .route("api_cards", r -> r.path("/accounts/{accountId}/cards/**")
                        .filters(f -> f.filter(jwtGatewayFilter))
                        .uri("http://localhost:8083")) // Ruta al microservicio api-cards
                .route("api_transfers", r -> r.path("/accounts/{accountId}/transferences/**")
                        .filters(f -> f.filter(jwtGatewayFilter))
                        .uri("http://localhost:9092")) // Ruta al microservicio api-transfers
                .build();
    }


}



