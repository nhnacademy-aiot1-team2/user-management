package com.user.management.service.impl;

import com.user.management.dto.*;
import com.user.management.entity.Role;
import com.user.management.entity.Status;
import com.user.management.entity.User;
import com.user.management.exception.*;
import com.user.management.page.RestPage;
import com.user.management.repository.ProviderRepository;
import com.user.management.repository.RoleRepository;
import com.user.management.repository.StatusRepository;
import com.user.management.repository.UserRepository;
import com.user.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 사용자 관련 서비스를 구현한 클래스입니다.
 * UserService 인터페이스로 구현되었습니다.
 *
 * @author jjunho50
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;
    private final ProviderRepository providerRepository;

    /**
     * 모든 사용자 정보를 가져옵니다. (관리자만 요청 가능)
     *
     * @param pageable 페이징 정보
     * @return 사용자 정보를 포함하는 Page<UserDataResponse> 객체
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "getUsers",
            key = "#pageable.pageSize.toString().concat('-').concat(#pageable.pageNumber)",
            cacheManager = "cacheManager",
            unless = "#result == null"
    )
    public RestPage<UserDataResponse> getAllUsers(Pageable pageable) {

        Page<UserDataResponse> allUserData = userRepository.getAllUserData(pageable);
        if (Objects.isNull(allUserData) || allUserData.getContent().isEmpty()) {
            throw new UserNotFoundException("user list empty");
        }

        return new RestPage<>(allUserData);
    }

    /**
     * 특정 statusId에 해당하는 사용자들의 정보를 페이징 처리하여 반환합니다. (관리자만 요청 가능)
     *
     * @param statusId 검색하려는 사용자 상태 ID.
     * @param pageable 페이징 정보.
     * @return UserDataResponse 객체의 페이지.
     * @throws RuntimeException 해당 statusId가 존재하지 않을 경우 발생.
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "getUsers",
            key = "#statusId.toString().concat('-').concat(#pageable.pageSize.toString()).concat('-').concat(#pageable.pageNumber)",
            cacheManager = "cacheManager",
            unless = "#result == null"
    )
    public RestPage<UserDataResponse> getFilteredUsersByStatus(Long statusId, Pageable pageable) {
        if (!statusRepository.existsById(statusId)) throw new RuntimeException("존재하지 않는 Status Id 입니다.");
        return new RestPage<>(userRepository.getUsersFilteredByStatusId(pageable, statusId));
    }


    /**
     * 주어진 ID에 해당하는 사용자의 정보를 반환하는 메소드입니다.
     *
     * @param id 조회하려는 사용자의 ID
     * @return UserDataResponse (id, name, email, roleName, statusName, password)
     * @throws UserHeaderNotFoundException X-USER-ID header 가 존재하지 않는 경우에 발생
     * @throws UserNotFoundException       사용자를 찾을 수 없을 때 발생하는 예외
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "getUser",
            key = "#id",
            cacheManager = "cacheManager",
            unless = "#result == null"
    )
    public UserDataResponse getUserById(String id) {
        if (id == null) throw new UserHeaderNotFoundException();
        return userRepository.getUserById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * userId로 Role 반환
     *
     * @param id userId
     * @return RoleResponse
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "getRole",
            key = "#id",
            cacheManager = "cacheManager",
            unless = "#result == null"
    )
    public RoleResponse getRoleByUserId(String id) {
        Role role = userRepository.getRoleByUserId(id);
        return new RoleResponse(role.getId(), role.getName());
    }

    /**
     * 사용자 로그인을 처리하는 메소드입니다.
     * 사용자 ID와 비밀번호를 통해 로그인을 하게 됩니다.
     *
     * @param userLoginRequest 사용자 로그인 요청 정보 (id, password)
     * @return 로그인된 User 정보
     * @throws UserNotFoundException            사용자를 찾을 수 없을 때 발생하는 예외
     * @throws InvalidPasswordException         비밀번호가 일치하지 않을 때 발생하는 예외
     * @throws AdminMustUpdatePasswordException 어드민은 첫 로그인 시, 초기 세팅 된 비밀번호를 반드시 변경해야 합니다.
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "getUserLogin",
            key = "#userLoginRequest.id",
            cacheManager = "cacheManager",
            unless = "#result == null"
    )
    public UserDataResponse getUserLogin(UserLoginRequest userLoginRequest) {
        User user = userRepository.findById(userLoginRequest.getId()).orElseThrow(() -> new UserNotFoundException(userLoginRequest.getId()));

        if (user.getRole().getId() == 1L && user.getLatestLoginAt() == null)
            throw new AdminMustUpdatePasswordException();

        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        return new UserDataResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().getName(), user.getStatus().getName(), user.getPassword());
    }


    /**
     * 새로운 사용자를 등록하는 메소드입니다.
     * 회원가입 날짜, 마지막 로그인 날짜 자동으로 LocalDateTime.now()로 등록
     *
     * @param userCreateRequest 사용자 생성 요청 정보 (id, name, password, email)
     * @throws UserAlreadyExistException  사용자가 이미 존재할 때 발생하는 예외
     * @throws AlreadyExistEmailException 이미 등록된 email 일 경우 발생하는 예외
     */
    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "getUsers", allEntries = true)
            }
    )
    public void createUser(UserCreateRequest userCreateRequest) {
        String userId = userCreateRequest.getId();
        String userEmail = userCreateRequest.getEmail();

        if (userRepository.existsById(userId)) throw new UserAlreadyExistException(userId);
        if (userRepository.getByEmail(userEmail).isPresent()) throw new AlreadyExistEmailException(userEmail);

        User user = User.builder().id(userCreateRequest.getId()).name(userCreateRequest.getName()).email(userEmail).password(passwordEncoder.encode(userCreateRequest.getPassword())).role(roleRepository.getUserRole()).status(statusRepository.getPendingStatus()).createdAt(LocalDateTime.now()).latestLoginAt(LocalDateTime.now()).provider(providerRepository.getDefaultProvider()).build();
        userRepository.save(user);

    }

    /**
     * 사용자의 상태를 'Active'로 변경합니다. (관리자만 요청 가능)
     *
     * @param permitUserRequest 변경 대상 사용자의 정보를 가지고 있는 객체.
     * @throws UserNotFoundException 해당 사용자가 존재하지 않을 경우 발생.
     */
    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "getUsers", allEntries = true, cacheManager = "cacheManager"),
                    @CacheEvict(value = "getUser", key = "#permitUserRequest.id", cacheManager = "cacheManager"),
                    @CacheEvict(value = "getUserLogin", key = "#permitUserRequest.id", cacheManager = "cacheManager")
            }
    )
    public void permitUser(PermitUserRequest permitUserRequest) {
        String userId = permitUserRequest.getId();
        User pendingUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.save(pendingUser.toBuilder().status(statusRepository.getActiveStatus()).latestLoginAt(LocalDateTime.now()).build());
    }

    /**
     * 사용자를 'Admin' Role로 변경합니다. (관리자만 요청 가능)
     *
     * @param permitUserRequest 변경 대상 사용자의 정보를 가지고 있는 객체.
     * @throws UserNotFoundException 해당 사용자가 존재하지 않을 경우 발생.
     */
    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "getUsers", allEntries = true, cacheManager = "cacheManager"),
                    @CacheEvict(value = "getUser", key = "#permitUserRequest.id", cacheManager = "cacheManager"),
                    @CacheEvict(value = "getUserLogin", key = "#permitUserRequest.id", cacheManager = "cacheManager"),
                    @CacheEvict(value = "getRole", key = "#permitUserRequest.id", cacheManager = "cacheManager")
            }
    )
    public void promoteUser(PermitUserRequest permitUserRequest) {
        String userId = permitUserRequest.getId();
        User pendingUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.save(pendingUser.toBuilder().role(roleRepository.getAdminRole()).latestLoginAt(LocalDateTime.now()).build());
    }

    /**
     * 사용자 정보를 업데이트하는 메소드입니다.
     * userId는 primary key 값으로 변경할 수 없습니다. Front Server 에서 UserCreateRequest.userId는 사용자가 아닌 서버가 등록할 수 있게 해주세요.
     *
     * @param userUpdateRequest 사용자 업데이트 요청 정보 (id, name, password, email)
     * @param userId            업데이트하려는 사용자의 ID
     * @throws UserHeaderNotFoundException X-USER-ID header가 존재하지 않는 경우에 발생
     * @throws UserNotFoundException       사용자를 찾을 수 없을 때 발생하는 예외
     * @throws AlreadyExistEmailException  이미 등록된 email 일 경우 발생하는 예외
     */
    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "getUsers", allEntries = true, cacheManager = "cacheManager"),
                    @CacheEvict(value = "getUser", key = "#userId", cacheManager = "cacheManager"),
                    @CacheEvict(value = "getUserLogin", key = "#userId", cacheManager = "cacheManager")
            }
    )
    public void updateUser(UserUpdateRequest userUpdateRequest, String userId) {
        if (userId == null) throw new UserHeaderNotFoundException();

        String userEmail = userUpdateRequest.getEmail();
        User existedUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        boolean emailExists = userRepository.getByEmail(userEmail).isPresent();
        if (!existedUser.getEmail().equals(userEmail) && emailExists) throw new AlreadyExistEmailException(userEmail);

        User user = existedUser.toBuilder().name(userUpdateRequest.getName()).email(userEmail).password(passwordEncoder.encode(userUpdateRequest.getPassword())).latestLoginAt(LocalDateTime.now()).status(statusRepository.getActiveStatus()).build();
        userRepository.save(user);
    }

    /**
     * 사용자를 비활성화 처리하는 메소드입니다.
     * 해당 사용자의 상태를 탈퇴 상태로 변경합니다.
     *
     * @param userId 탈퇴 처리하려는 사용자의 ID
     * @throws UserHeaderNotFoundException X-USER-ID header 가 존재하지 않는 경우에 발생
     * @throws UserNotFoundException       사용자를 찾을 수 없을 때 발생하는 예외
     */
    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "getUsers", allEntries = true, cacheManager = "cacheManager")
            }
    )
    public void deactivateUser(String userId) {
        if (userId == null) throw new UserHeaderNotFoundException();

        User existedUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        User user = existedUser.toBuilder().latestLoginAt(LocalDateTime.now()).status(statusRepository.getDeactivatedStatus()).build();
        userRepository.save(user);
    }


    /**
     * 사용자의 정보를 삭제하는 메소드입니다. (관리자만 요청 가능)
     *
     * @param deleteUserRequest 삭제 처리하려는 사용자의 ID
     * @throws UserNotFoundException 사용자의 userId가 존재하지 않을 경우 이 예외를 발생시킵니다.
     */
    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "getUsers", allEntries = true, cacheManager = "cacheManager"),
                    @CacheEvict(value = "getUserLogin", key = "#deleteUserRequest.id", cacheManager = "cacheManager")
            }
    )
    public void deleteUser(DeleteUserRequest deleteUserRequest) {
        String userId = deleteUserRequest.getId();
        if (!userRepository.existsById(userId)) throw new RuntimeException("이미 존재하지 않는 userId 입니다.");
        userRepository.deleteById(userId);
    }

    /**
     * 매일 0시에 따라 사용자의 최종 로그인 시간을 확인하고,
     * 마지막 로그인 시간이 한달 이상 전이면 사용자의 상태를 '휴면' 상태로 변경하는 스케줄러입니다.
     */
    @Transactional
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
    protected void checkAndUpdateInactivity(User existedUser) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        // Admin을 제외한 모든 User는 회원가입 시, latestLoginAt 값을 가짐.
        if (existedUser.getLatestLoginAt() == null) return;

        if (existedUser.getLatestLoginAt().isBefore(oneMonthAgo)) {
            Status inActiveStatus = statusRepository.getInActiveStatus();
            User user = existedUser.toBuilder().status(inActiveStatus).build();
            userRepository.save(user);
        }
    }
}

