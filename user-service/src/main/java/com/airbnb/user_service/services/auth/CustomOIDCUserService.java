package com.airbnb.user_service.services.auth;

import com.airbnb.user_service.entities.User;
import com.airbnb.user_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOIDCUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {
    private final OidcUserService oidcUserService = new OidcUserService();
    private final UserService userService;

    @Override
    public @Nullable OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser user = oidcUserService.loadUser(userRequest);
        User _ = userService.createOrUpdateUser(
                userRequest.getClientRegistration().getRegistrationId(),
                user
        );
        return user;
    }
}
