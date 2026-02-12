package com.ahmete.budget_app.common.config;

import com.ahmete.budget_app.apikey.repository.ApiKeyRepository;
import com.ahmete.budget_app.common.security.ApiErrorAuthenticationEntryPoint;
import com.ahmete.budget_app.common.security.ApiKeyAuthFilter;
import com.ahmete.budget_app.common.security.ApiKeyHasher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.ahmete.budget_app.auth.security.JwtAuthFilter;
import com.ahmete.budget_app.auth.service.JwtService;

@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            ApiKeyRepository apiKeyRepository,
            ApiKeyHasher hasher,
            JwtService jwtService
    ) throws Exception {
        
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // auth endpoints
                .requestMatchers("/api/v1/auth/**").permitAll()
                
                // onboarding
                .requestMatchers("/api/v1/users/**").permitAll()
                .requestMatchers("/api/v1/api-keys/**").permitAll()
                
                // swagger
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                
                .anyRequest().authenticated()
        );
        
        // JWT first (Bearer)
        http.addFilterBefore(new JwtAuthFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
        
        // API KEY next (X-API-KEY)
        http.addFilterBefore(new ApiKeyAuthFilter(apiKeyRepository, hasher), UsernamePasswordAuthenticationFilter.class);
        
        http.formLogin(form -> form.disable());
        http.httpBasic(basic -> basic.disable());
        
        return http.build();
    }
    
}