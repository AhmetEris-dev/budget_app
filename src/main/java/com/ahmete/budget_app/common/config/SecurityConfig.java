package com.ahmete.budget_app.common.config;

import com.ahmete.budget_app.apikey.repository.ApiKeyRepository;
import com.ahmete.budget_app.common.security.ApiKeyAuthFilter;
import com.ahmete.budget_app.common.security.ApiKeyHasher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            ApiKeyRepository apiKeyRepository,
            ApiKeyHasher hasher,
            ObjectMapper objectMapper
    ) throws Exception {
        
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        http.exceptionHandling(ex -> ex
                // Auth yoksa -> 401
                .authenticationEntryPoint((req, res, e) -> {
                    res.setStatus(401);
                    res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    
                    Map<String, Object> body = new LinkedHashMap<>();
                    body.put("status", 401);
                    body.put("error", "Unauthorized");
                    body.put("message", "Missing or invalid X-API-KEY header");
                    body.put("path", req.getRequestURI());
                    
                    objectMapper.writeValue(res.getOutputStream(), body);
                })
                // Auth var ama yetki yoksa -> 403
                .accessDeniedHandler((req, res, e) -> {
                    res.setStatus(403);
                    res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    
                    Map<String, Object> body = new LinkedHashMap<>();
                    body.put("status", 403);
                    body.put("error", "Forbidden");
                    body.put("message", "Access denied");
                    body.put("path", req.getRequestURI());
                    
                    objectMapper.writeValue(res.getOutputStream(), body);
                })
        );
        
        http.authorizeHttpRequests(auth -> auth
                // preflight
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                
                // onboarding (public)
                .requestMatchers("/api/v1/users/**").permitAll()
                
                // swagger (public)
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                
                // API key yönetimi PUBLIC OLMAZ -> auth ister
                .requestMatchers("/api/v1/api-keys/**").authenticated()
                
                // diğer her şey -> auth ister
                .anyRequest().authenticated()
        );
        
        http.addFilterBefore(
                new ApiKeyAuthFilter(apiKeyRepository, hasher, objectMapper),
                UsernamePasswordAuthenticationFilter.class
        );
        
        http.formLogin(form -> form.disable());
        http.httpBasic(basic -> basic.disable());
        
        return http.build();
    }
}