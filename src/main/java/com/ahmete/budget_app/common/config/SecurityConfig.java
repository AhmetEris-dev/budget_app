package com.ahmete.budget_app.common.config;

import com.ahmete.budget_app.apikey.repository.ApiKeyRepository;
import com.ahmete.budget_app.common.security.ApiKeyAuthFilter;
import com.ahmete.budget_app.common.security.ApiKeyHasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            ApiKeyRepository apiKeyRepository,
            ApiKeyHasher hasher
    ) throws Exception {
        
        http.csrf(csrf -> csrf.disable());
        
        http.authorizeHttpRequests(auth -> auth
                // onboarding
                .requestMatchers("/api/v1/users/**").permitAll()
                .requestMatchers("/api/v1/api-keys/**").permitAll()
                
                // swagger
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                
                // others require auth
                .anyRequest().authenticated()
        );
        
        http.addFilterBefore(
                new ApiKeyAuthFilter(apiKeyRepository, hasher),
                UsernamePasswordAuthenticationFilter.class
        );
        
        http.formLogin(form -> form.disable());
        http.httpBasic(basic -> basic.disable());
        
        return http.build();
    }
}