package com.user.management.service.impl;

import com.user.management.dto.UserCreateRequest;
import com.user.management.dto.UserLoginRequest;
import com.user.management.entity.User;
import com.user.management.exception.InvalidPasswordException;
import com.user.management.exception.UserAlreadyExistException;
import com.user.management.exception.UserNotFoundException;
import com.user.management.exception.UserOnlyUpdateOwnData;
import com.user.management.repository.RoleRepository;
import com.user.management.repository.StatusRepository;
import com.user.management.repository.UserRepository;
import com.user.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;
    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User getUserLogin(UserLoginRequest userLoginRequest)
    {
        User user = userRepository.findById(userLoginRequest.getId()).orElseThrow(() -> new UserNotFoundException(userLoginRequest.getId()));
        if (!user.getPassword().equals(userLoginRequest.getPassword())) {
            throw new InvalidPasswordException();
        }
        return user;
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
                .role(roleRepository.getUserRole())
                .status(statusRepository.getActiveStatus())
                .createdAt(LocalDateTime.now())
                .latestLoginAt(LocalDateTime.now())
                // 회원가입을 해도, 로그인을 하기 전까지는 null로 두려 했는데, 휴면 상태인지 체크할때 null이 문제될까봐 회원가입과 동시에 로그인 날짜가 갱신된다.
                .build();

        userRepository.save(user);

    }

    @Override
    public void updateUser(UserCreateRequest userCreateRequest, String userId) {
        if(!userId.equals(userCreateRequest.getId()))
            throw new UserOnlyUpdateOwnData();

        User existedUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        User user = User.builder()
                .id(userCreateRequest.getId())
                .name(userCreateRequest.getName())
                .email(userCreateRequest.getEmail())
                .birth(userCreateRequest.getBirth())
                .password(userCreateRequest.getPassword())
                .createdAt(existedUser.getCreatedAt())
                .latestLoginAt(LocalDateTime.now())
                .status(statusRepository.getActiveStatus())
                .role(roleRepository.getUserRole())
                .build();

        userRepository.save(user);

    }

    @Override
    public void deleteUser(String userId) {
        User existedUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        User user = User.builder()
                .id(existedUser.getId())
                .name(existedUser.getName())
                .email(existedUser.getEmail())
                .birth(existedUser.getBirth())
                .password(existedUser.getPassword())
                .createdAt(existedUser.getCreatedAt())
                .role(existedUser.getRole())
                .latestLoginAt(LocalDateTime.now())
                .status(statusRepository.getDeactivatedStatus())
                .build();

        userRepository.save(user);
    }
}
