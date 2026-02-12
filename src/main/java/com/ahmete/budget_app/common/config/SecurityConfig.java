package com.ahmete.budget_app.common.config;

import com.ahmete.budget_app.apikey.repository.ApiKeyRepository;
import com.ahmete.budget_app.auth.security.JwtAuthFilter;
import com.ahmete.budget_app.auth.service.JwtService;
import com.ahmete.budget_app.common.security.ApiKeyAuthFilter;
import com.ahmete.budget_app.common.security.ApiKeyHasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            ApiKeyRepository apiKeyRepository,
            ApiKeyHasher hasher,
            JwtService jwtService
    ) throws Exception {
        
        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(jwtService);
        ApiKeyAuthFilter apiKeyAuthFilter = new ApiKeyAuthFilter(apiKeyRepository, hasher);
        
        return http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // ✅ Sıra: JWT -> API KEY -> UsernamePassword...
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(apiKeyAuthFilter, JwtAuthFilter.class)
                
                .authorizeHttpRequests(auth -> auth
                        // ✅ Public: Swagger
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        
                        // ✅ Public: Auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        
                        // (Öneri) User register artık auth altında.
                        // Eğer POST /api/v1/users kaldırdıysan burada permitAll’a gerek yok.
                        // .requestMatchers("/api/v1/users").permitAll()
                        
                        .anyRequest().authenticated()
                )
                .build();
    }
}