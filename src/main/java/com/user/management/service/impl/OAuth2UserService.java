package com.user.management.service.impl;

import com.user.management.entity.Provider;
import com.user.management.entity.User;
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
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.ProviderNotFoundException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * oauth service class
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;


    /**
     * OAuth2 인증 플로우 후에 사용자 정보를 로드하는 메소드입니다.
     * <p>
     * 새로운 사용자의 요청인 경우 User 인스턴스를 새로 생성하고,
     * 기존 사용자의 경우 마지막 로그인 시간만 업데이트합니다.
     * <p>
     * OAuth2User 객체를 반환하므로, 이후 처리는 Spring Security에 의해 수행됩니다.
     *
     * @param userRequest OAuth2 인증 플로우 중에 생성된 사용자 정보가 담긴 요청 객체.
     * @return 인증된 사용자를 나타내는 OAuth2User 객체.
     * @throws OAuth2AuthenticationException 인증 중에 오류가 발생하거나,
     *                                       유효하지 않은 OAuth2 요청이 수신된 경우 던져집니다.
     */
    @Override
    @Transactional(readOnly = true)
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String providerId = oAuth2User.getAttribute("sub"); // GoogleId
        if (providerId == null)
            providerId = Objects.requireNonNull(oAuth2User.getAttribute("id")).toString(); // GithubId

        String email = oAuth2User.getAttribute("email"); // GoogleId
        if (email == null) email = oAuth2User.getAttribute("login") + "@example.com"; // GithubId

        String nameKey = userRequest.getClientRegistration().getClientId();
        Provider provider = providerRepository.findByName(nameKey).orElseThrow(ProviderNotFoundException::new);
        String username = provider.getId() + "_" + providerId; //중복이 발생하지 않도록 provider와 providerId를 조합

        User existedUser = userRepository.findById(username).orElse(null);
        if (existedUser == null) {
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
        } else {
            userRepository.save(existedUser.toBuilder().latestLoginAt(LocalDateTime.now()).build());
        }
        return oAuth2User;
    }
}
