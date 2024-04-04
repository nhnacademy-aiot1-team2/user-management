package com.user.management.service.impl;

import com.user.management.entity.Provider;
import com.user.management.entity.User;
import com.user.management.exception.AlreadyExistEmailException;
import com.user.management.repository.ProviderRepository;
import com.user.management.repository.RoleRepository;
import com.user.management.repository.StatusRepository;
import com.user.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.nio.file.ProviderNotFoundException;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String providerId = oAuth2User.getAttribute("sub");
        if(providerId == null)
            providerId = Objects.requireNonNull(oAuth2User.getAttribute("id")).toString();

        String email = oAuth2User.getAttribute("email");
        if(email == null) email = oAuth2User.getAttribute("login") + "@example.com";

        if(userRepository.getByEmail(email).isPresent()) throw new AlreadyExistEmailException(email);

        String nameKey = userRequest.getClientRegistration().getClientId();
        Provider provider = providerRepository.findByName(nameKey)
                .orElseThrow(ProviderNotFoundException::new);
        String username = provider.getId() + "_" + providerId; //중복이 발생하지 않도록 provider와 providerId를 조합

        if (!userRepository.existsById(username)) {
            User user = User.builder()
                    .id(username)
                    .name(username)
                    .email(email)
                    .password(encoder.encode(username))
                    .role(roleRepository.getUserRole())
                    .status(statusRepository.getActiveStatus())
                    .createdAt(LocalDateTime.now())
                    .latestLoginAt(LocalDateTime.now())
                    .provider(provider).build();
            userRepository.save(user);
        }
        return oAuth2User;
    }
}
