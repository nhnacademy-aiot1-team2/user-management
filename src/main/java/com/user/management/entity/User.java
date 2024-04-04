package com.user.management.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
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



    @Builder

    public User(String id, String name, String password, String email, Role role, Status status, Provider provider, LocalDateTime createdAt, LocalDateTime latestLoginAt) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.status = status;
        this.provider = provider;
        this.createdAt = createdAt;
        this.latestLoginAt = latestLoginAt;
    }
}