package com.airbnb.user_service.controllers;

import com.airbnb.user_service.dtos.request.RefreshTokenRequest;
import com.airbnb.user_service.dtos.response.TokenResponse;
import com.airbnb.user_service.exceptions.ApiException;
import com.airbnb.user_service.payloads.ApiResponse;
import com.airbnb.user_service.services.AuthTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${app.gateway-base-url}")
    private String gatewayBaseUrl;

    private final Set<String> AUTH_PROVIDERS = Set.of("google", "github");

    private final AuthTokenService authTokenService;

    @GetMapping("/login-uri/{provider}")
    public RedirectView login(@PathVariable String provider) {
        if (!AUTH_PROVIDERS.contains(provider)) throw new ApiException("Invalid provider", HttpStatus.BAD_REQUEST);
        return new RedirectView(gatewayBaseUrl + "/oauth2/authorization/" + provider);
    }

    @GetMapping("/get-tokens")
    public ResponseEntity<ApiResponse<TokenResponse>> getTokens(Authentication authentication) {
        TokenResponse tokenResponse = authTokenService.issueTokens(authentication);
        return ResponseEntity.ok(ApiResponse.success("Authenticated", tokenResponse));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(ApiResponse.success(
                "Tokens refreshed",
                authTokenService.refreshToken(refreshTokenRequest)
        ));
    }

    @GetMapping("/error")
    public ResponseEntity<ApiResponse<Void>> error() {
        ApiResponse<Void> apiResponse = ApiResponse.error("Authentication failed");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
    }

}
