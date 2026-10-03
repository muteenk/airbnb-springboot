package com.airbnb.user_service.services;

import com.airbnb.user_service.entities.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface UserService {
    User createOrUpdateUser(String provider, OAuth2User user);
}
