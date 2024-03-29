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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;

    /**
     * 모든 사용자 정보를 가져옵니다. (관리자만 요청 가능)
     *
     * @param id       검증할 사용자 ID, 없으면 예외 발생
     * @param pageable 페이징 정보
     * @return         사용자 정보를 포함하는 Page<UserDataResponse> 객체
     * @throws UserNotFoundException 이 메서드는 사용자 ID가 존재하지 않을 경우 이 예외를 발생시킵니다.
     * @throws OnlyAdminCanAccessUserDataException 이 메서드는 사용자의 역할이 관리자가 아닐 경우 이 예외를 발생시킵니다.
     */
    @Override
    public Page<UserDataResponse> getAllUsers(String id, Pageable pageable)
    {
        if(!userRepository.existsById(id)) throw new UserNotFoundException(id);

        if(userRepository.getRoleByUserId(id).getId() != 1L)
            throw new OnlyAdminCanAccessUserDataException();

        return userRepository.getAllUserData(pageable);
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
     * @throws AdminMustUpdatePasswordException 어드민은 첫 로그인 시, 초기 세팅 된 비밀번호를 반드시 변경해야 합니다.
     */
    @Override
    public UserDataResponse getUserLogin(UserLoginRequest userLoginRequest) {
        User user = userRepository.findById(userLoginRequest.getId()).orElseThrow(() -> new UserNotFoundException(userLoginRequest.getId()));

        if(user.getRole().getId() == 1L && user.getLatestLoginAt() == null)
            throw new AdminMustUpdatePasswordException();

        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        return new UserDataResponse(user.getId(), user.getName(), user.getEmail()
                , user.getBirth(), user.getRole().getName(), user.getStatus().getName());
    }

    /**
     * 새로운 사용자를 등록하는 메소드입니다.
     * 등록하려는 사용자 ID가 이미 대응하는 사용자가 있는 경우 예외를 발생시킵니다.
     *
     * @param userCreateRequest 사용자 생성 요청 정보 (id, name, password, email, birth)
     * @throws UserAlreadyExistException 사용자가 이미 존재할 때 발생하는 예외
     * @throws AlreadyExistEmailException 이미 등록된 email 일 경우 발생하는 예외
     */
    @Override
    public void createUser(UserCreateRequest userCreateRequest) {
        String userId = userCreateRequest.getId();
        String userEmail = userCreateRequest.getEmail();

        if(userRepository.existsById(userId))
            throw new UserAlreadyExistException(userId);

        if(userRepository.getByEmail(userEmail).orElse(null) != null)
            throw new AlreadyExistEmailException(userEmail);

        User user = User.builder()
                .id(userCreateRequest.getId())
                .name(userCreateRequest.getName())
                .email(userEmail)
                .birth(userCreateRequest.getBirth())
                .password(passwordEncoder.encode(userCreateRequest.getPassword()))
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
     * userId는 primary key 값으로 변경할 수 없습니다. Front Server 에서 userId는 사용자가 아닌 서버가 등록할 수 있게 해주세요.
     * @param userCreateRequest 사용자 업데이트 요청 정보 (id, name, password, email, birth)
     * @param userId 업데이트하려는 사용자의 ID
     * @throws UserNotFoundException 사용자를 찾을 수 없을 때 발생하는 예외
     * @throws UserAlreadyExistException 변경하는 userId가 이미 존재하는 userId 일 때 발생하는 예외
     * @throws AlreadyExistEmailException 이미 등록된 email 일 경우 발생하는 예외
     */
    @Override
    public void updateUser(UserCreateRequest userCreateRequest, String userId) {
        String userEmail = userCreateRequest.getEmail();
        User existedUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        boolean emailExists = userRepository.getByEmail(userEmail).isPresent();
        if(!existedUser.getEmail().equals(userEmail) && emailExists)
            throw new AlreadyExistEmailException(userEmail);

        User user = existedUser.toBuilder()
                .name(userCreateRequest.getName())
                .email(userEmail)
                .birth(userCreateRequest.getBirth())
                .password(passwordEncoder.encode(userCreateRequest.getPassword()))
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

        // Admin을 제외한 모든 User는 회원가입 시, latestLoginAt 값을 가짐.
        if(existedUser.getLatestLoginAt() == null)
            return;

        if(existedUser.getLatestLoginAt().isBefore(oneMonthAgo)) {
            Status inActiveStatus = statusRepository.getInActiveStatus();

            User user = existedUser.toBuilder()
                    .status(inActiveStatus)
                    .build();

            userRepository.save(user);
        }
    }
}

