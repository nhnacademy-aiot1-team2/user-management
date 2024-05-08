package com.user.management.entity;

import com.user.management.dto.UserDataResponse;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * user entity class
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    private String id;

    @Column(name = "user_name")
    private String name; // 이름

    @Column(name = "user_password")
    private String password; // 비밀번호

    @Column(name = "user_email")
    private String email; // 이메일

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @OneToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @Column(name = "created_at")
    private LocalDateTime createdAt; // 회원가입일자

    @Column(name = "latest_login_at")
    private LocalDateTime latestLoginAt; // 마지막 접속일

    public UserDataResponse toEntity() {
        return UserDataResponse.builder()
                .id(id)
                .name(name)
                .email(email)
                .roleName(role.getName())
                .statusName(status.getName())
                .password(password)
                .build();
    }
}