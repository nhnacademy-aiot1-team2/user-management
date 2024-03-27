package com.user.management.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Users")
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

    @Column(name = "user_birth")
    private String birth; // 생일(8자리)

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "created_at")
    private LocalDateTime createdAt; // 회원가입일자

    @Column(name = "latest_login_at")
    private LocalDateTime latestLoginAt; // 마지막 접속일



    @Builder
    public User(String id, String name, String password, String email, String birth, Role role, Status status, LocalDateTime createdAt, LocalDateTime latestLoginAt) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.birth = birth;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.latestLoginAt = latestLoginAt;
    }
}