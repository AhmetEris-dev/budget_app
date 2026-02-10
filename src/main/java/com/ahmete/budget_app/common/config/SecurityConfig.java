package com.ahmete.budget_app.common.config;

import com.ahmete.budget_app.auth.filter.ApiKeyAuthFilter;
import com.ahmete.budget_app.auth.repository.ApiKeyRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            ApiKeyRepository apiKeyRepository
    ) throws Exception {
        
        http.csrf(csrf -> csrf.disable());
        
        http.sessionManagement(sm ->
                                       sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/api/v1/api-keys/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/actuator/health"
                ).permitAll()
                .anyRequest().authenticated()
        );
        
        http.addFilterBefore(
                new ApiKeyAuthFilter(apiKeyRepository),
                UsernamePasswordAuthenticationFilter.class
        );
        
        http.httpBasic(Customizer.withDefaults());
        http.formLogin(form -> form.disable());
        
        return http.build();
    }
}