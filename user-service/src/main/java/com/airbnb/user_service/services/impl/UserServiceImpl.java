package com.airbnb.user_service.services.impl;

import com.airbnb.user_service.entities.User;
import com.airbnb.user_service.repositories.UserRepository;
import com.airbnb.user_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createOrUpdateUser(String provider, OAuth2User authUser) {
        String subject = authUser.getName();
        String email = authUser.getAttribute("email");
        String name = authUser.getAttribute("name");

        Optional<User> existingUser = userRepository.findByProviderAndProviderSubject(
                provider, subject
        );

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setEmail(email);
            user.setName(name);
            return user;
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .provider(provider)
                .providerSubject(subject)
                .build();
        return userRepository.save(user);
    }
}
