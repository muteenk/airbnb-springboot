package com.airbnb.user_service.config;

import com.airbnb.user_service.services.auth.CustomDefaultAuthUserService;
import com.airbnb.user_service.services.auth.CustomOIDCUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${app.gateway-base-url}")
    private String gatewayBaseUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CustomOIDCUserService customOIDCUserService,
            CustomDefaultAuthUserService customDefaultAuthUserService
    ) {

        String successUrl = gatewayBaseUrl + "/users/v1/auth/get-tokens";
        String errorUrl = gatewayBaseUrl + "/users/v1/auth/error";

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                    auth
                            .requestMatchers(
                                    "/v1/auth/login-uri/**",
                                    "/v1/auth/refresh-token",
                                    "/v1/auth/error",
                                    "/oauth2/**",
                                    "/login/oauth2/**",
                                    "/actuator/**"
                            ).permitAll()
                            .anyRequest().authenticated()
                )
                .oauth2Login(configurer ->
                    configurer.userInfoEndpoint( userInfoEndpointConfig -> {
                        userInfoEndpointConfig.oidcUserService(customOIDCUserService);
                        userInfoEndpointConfig.userService(customDefaultAuthUserService);
                    })
                    .defaultSuccessUrl(successUrl, true)
                    .failureUrl(errorUrl)
                );
        return http.build();
    }
}
