package com.airbnb.user_service.services.impl;

import com.airbnb.user_service.dtos.request.RefreshTokenRequest;
import com.airbnb.user_service.dtos.response.TokenResponse;
import com.airbnb.user_service.entities.User;
import com.airbnb.user_service.exceptions.ApiException;
import com.airbnb.user_service.exceptions.ResourceDoesNotExist;
import com.airbnb.user_service.repositories.UserRepository;
import com.airbnb.user_service.services.AuthTokenService;
import com.airbnb.user_service.utilities.JWTUtility;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {

    private final JWTUtility jwtUtility;
    private final UserRepository userRepository;

    @Override
    public TokenResponse issueTokens(Authentication authentication) {
        if (!(authentication instanceof OAuth2AuthenticationToken oauthToken)) {
            throw new ApiException("OAuth Login required !", HttpStatus.BAD_REQUEST);
        }

        String provider = oauthToken.getAuthorizedClientRegistrationId();
        String subject = oauthToken.getName();

        User user = userRepository
                .findByProviderAndProviderSubject(provider, subject)
                .orElseThrow(() -> new ResourceDoesNotExist("User not found !"));

        return new TokenResponse(
                jwtUtility.generateAccessToken(user),
                jwtUtility.generateRefreshToken(user)
        );
    }

    @Override
    public TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.refreshToken();
        Claims userClaim = jwtUtility.parseRefreshToken(refreshToken);
        Long userId = Long.valueOf(userClaim.getSubject());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceDoesNotExist("User doesn't exist"));

        return new TokenResponse(
                jwtUtility.generateAccessToken(user),
                jwtUtility.generateRefreshToken(user)
        );
    }
}
