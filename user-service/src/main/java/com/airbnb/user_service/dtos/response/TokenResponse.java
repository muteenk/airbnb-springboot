package com.airbnb.user_service.dtos.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
