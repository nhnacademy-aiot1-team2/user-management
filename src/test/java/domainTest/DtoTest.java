package domainTest;

import com.user.management.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Slf4j
public class DtoTest {
    private static Validator validator;

    @BeforeAll
    public static void init() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("createDto 유효성 검사")
    void userCreateRequest_validtaion() {
        UserCreateRequest userCreateRequest = new UserCreateRequest("id", "name", "password", "email@do.com");
        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(userCreateRequest);

        for(ConstraintViolation<UserCreateRequest> violation : violations) {
            log.error(violation.getMessage());
        }
    }

    @Test
    @DisplayName("loginDto 유효성 검사")
    void userLoginRequest_validtaion() {
        UserLoginRequest userLoginRequest = new UserLoginRequest("hi", "password");
        Set<ConstraintViolation<UserLoginRequest>> violations = validator.validate(userLoginRequest);

        for(ConstraintViolation<UserLoginRequest> violation : violations) {
            log.error(violation.getMessage());
        }
    }

    @Test
    @DisplayName("deleteDto 유효성 검사")
    void userDeleteRequest_validtaion() {
        DeleteUserRequest deleteUserRequest = new DeleteUserRequest("id");
        Set<ConstraintViolation<DeleteUserRequest>> violations = validator.validate(deleteUserRequest);

        for(ConstraintViolation<DeleteUserRequest> violation : violations) {
            log.error(violation.getMessage());
        }
    }

    @Test
    @DisplayName("permitDto 유효성 검사")
    void userPermitRequest_validtaion() {
        PermitUserRequest permitUserRequest = new PermitUserRequest("id");
        Set<ConstraintViolation<PermitUserRequest>> violations = validator.validate(permitUserRequest);

        for(ConstraintViolation<PermitUserRequest> violation : violations) {
            log.error(violation.getMessage());
        }
    }

    @Test
    @DisplayName("updateDto 유효성 검사")
    void userUpdateRequest_validtaion() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("id", "password", "hi@do.com");
        Set<ConstraintViolation<UserUpdateRequest>> violations = validator.validate(userUpdateRequest);

        for(ConstraintViolation<UserUpdateRequest> violation : violations) {
            log.error(violation.getMessage());
        }
    }
}
