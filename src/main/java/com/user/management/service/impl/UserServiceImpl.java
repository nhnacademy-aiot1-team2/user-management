package com.user.management.service.impl;

import com.user.management.dto.UserCreateRequest;
import com.user.management.entity.Role;
import com.user.management.entity.User;
import com.user.management.exception.UserAlreadyExistException;
import com.user.management.exception.UserNotFoundException;
import com.user.management.repository.RoleRepository;
import com.user.management.repository.UserRepository;
import com.user.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public void createUser(UserCreateRequest userCreateRequest) {

        String userId = userCreateRequest.getId();

        if(userRepository.existsById(userId))
            throw new UserAlreadyExistException(userId);

        User user = User.builder()
                .id(userCreateRequest.getId())
                .name(userCreateRequest.getName())
                .email(userCreateRequest.getEmail())
                .birth(userCreateRequest.getBirth())
                .password(userCreateRequest.getPassword())
                .role(roleRepository.findByName("ROLE_USER"))
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

    }

    @Override
    public void updateUser(UserCreateRequest userCreateRequest, String userId) {
        User existedUser = userRepository.findById(userId).orElse(null);

        User user = User.builder()
                .id(userCreateRequest.getId())
                .name(userCreateRequest.getName())
                .email(userCreateRequest.getEmail())
                .birth(userCreateRequest.getBirth())
                .password(userCreateRequest.getPassword())
                .createdAt(existedUser.getCreatedAt())
                .latestLoginAt(LocalDateTime.now())
                .role(roleRepository.findByName("ROLE_USER"))
                .build();

        userRepository.save(user);

    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
