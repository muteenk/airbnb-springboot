package com.airbnb.user_service.services;

import com.airbnb.user_service.dtos.request.RefreshTokenRequest;
import com.airbnb.user_service.dtos.response.TokenResponse;
import org.springframework.security.core.Authentication;

public interface AuthTokenService {
    TokenResponse issueTokens(Authentication authentication);

    TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
