package com.user.management.service.impl;

import com.user.management.dto.UserCreateRequest;
import com.user.management.dto.UserDataResponse;
import com.user.management.dto.UserLoginRequest;
import com.user.management.entity.Status;
import com.user.management.entity.User;
import com.user.management.exception.*;
import com.user.management.repository.RoleRepository;
import com.user.management.repository.StatusRepository;
import com.user.management.repository.UserRepository;
import com.user.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 사용자 관련 서비스를 구현한 클래스입니다.
 * UserService 인터페이스를 구현합니다.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;

    /**
     * 모든 사용자의 정보를 반환하는 메소드입니다.
     * (오직 관리자만 접근 가능한 메소드입니다.)
     *
     * @param id 인증된 사용자의 ID
     * @return List<UserDataResponse>
     * @throws UserNotFoundException 사용자를 찾을 수 없을 때 발생하는 예외
     * @throws OnlyAdminCanAccessUserDataException 인증된 사용자가 관리자 역할을 가지지 않을 때 발생하는 예외
     */
    @Override
    public List<UserDataResponse> getAllUsers(String id)
    {
        if(!userRepository.existsById(id)) throw new UserNotFoundException(id);

        if(userRepository.getRoleByUserId(id).getId() != 1L)
            throw new OnlyAdminCanAccessUserDataException();

        return userRepository.getAllUserData();
    }

    /**
     * 주어진 ID에 해당하는 사용자의 정보를 반환하는 메소드입니다.
     *
     * @param id 조회하려는 사용자의 ID
     * @return UserDataResponse (id, name, email, birth, roleName, statusName)
     * @throws UserNotFoundException 사용자를 찾을 수 없을 때 발생하는 예외
     */
    @Override
    public UserDataResponse getUserById(String id) {
        return userRepository.getUserById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * 사용자 로그인을 처리하는 메소드입니다.
     * 사용자 ID와 비밀번호를 통해 로그인을 하게 됩니다.
     *
     * @param userLoginRequest 사용자 로그인 요청 정보 (id, password)
     * @return 로그인된 User 정보
     * @throws UserNotFoundException 사용자를 찾을 수 없을 때 발생하는 예외
     * @throws InvalidPasswordException 비밀번호가 일치하지 않을 때 발생하는 예외
     */
    @Override
    public User getUserLogin(UserLoginRequest userLoginRequest)
    {
        User user = userRepository.findById(userLoginRequest.getId()).orElseThrow(() -> new UserNotFoundException(userLoginRequest.getId()));
        if (!user.getPassword().equals(userLoginRequest.getPassword())) {
            throw new InvalidPasswordException();
        }
        return user;
    }

    /**
     * 새로운 사용자를 등록하는 메소드입니다.
     * 등록하려는 사용자 ID가 이미 대응하는 사용자가 있는 경우 예외를 발생시킵니다.
     *
     * @param userCreateRequest 사용자 생성 요청 정보 (id, name, password, email, birth)
     * @throws UserAlreadyExistException 사용자가 이미 존재할 때 발생하는 예외
     */
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

    /**
     * 사용자 정보를 업데이트하는 메소드입니다.
     * 요청 사용자와 변경하려는 사용자가 같지 않으면 예외를 발생시킵니다.
     *
     * @param userCreateRequest 사용자 업데이트 요청 정보 (id, name, password, email, birth)
     * @param userId 업데이트하려는 사용자의 ID
     * @throws UserOnlyUpdateOwnDataException 요청 사용자와 변경하려는 사용자가 다를 때 발생하는 예외
     * @throws UserNotFoundException 사용자를 찾을 수 없을 때 발생하는 예외
     */
    @Override
    public void updateUser(UserCreateRequest userCreateRequest, String userId) {
        if(!userId.equals(userCreateRequest.getId()))
            throw new UserOnlyUpdateOwnDataException();

        User existedUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        User user = existedUser.toBuilder()
                .id(userCreateRequest.getId())
                .name(userCreateRequest.getName())
                .email(userCreateRequest.getEmail())
                .birth(userCreateRequest.getBirth())
                .password(userCreateRequest.getPassword())
                .latestLoginAt(LocalDateTime.now())
                .status(statusRepository.getActiveStatus())
                .build();

        userRepository.save(user);
    }

    /**
     * 사용자를 탈퇴 처리하는 메소드입니다.
     * 해당 사용자의 상태를 탈퇴 상태로 변경합니다.
     *
     * @param userId 탈퇴 처리하려는 사용자의 ID
     * @throws UserNotFoundException 사용자를 찾을 수 없을 때 발생하는 예외
     */
    @Override
    public void deleteUser(String userId) {
        User existedUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        User user = existedUser.toBuilder()
                .latestLoginAt(LocalDateTime.now())
                .status(statusRepository.getDeactivatedStatus())
                .build();

        userRepository.save(user);


    }

    /**
     * 매일 0시에 따라 사용자의 최종 로그인 시간을 확인하고,
     * 마지막 로그인 시간이 한달 이상 전이면 사용자의 상태를 '휴면' 상태로 변경하는 스케줄러입니다.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateUserInactivityStatus() {
        List<User> users = userRepository.findAll();
        users.forEach(this::checkAndUpdateInactivity);
    }

    /**
     * 주어진 사용자의 'latestLoginAt' 필드(마지막 로그인 시간)를 확인하고,
     * 이 시간이 현재 시간으로부터 한 달 이상 이전인 경우, 사용자의 상태를 '휴면'으로 변경합니다.
     *
     * @param existedUser 휴면 상태를 확인할 사용자
     */
    public void checkAndUpdateInactivity(User existedUser) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        if(existedUser.getLatestLoginAt().isBefore(oneMonthAgo)) {
            Status inActiveStatus = statusRepository.getInActiveStatus();

            User user = existedUser.toBuilder()
                    .status(inActiveStatus)
                    .build();

            userRepository.save(user);
        }
    }
}

