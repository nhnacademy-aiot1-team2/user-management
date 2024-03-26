package com.user.management.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_status")
public class Status {
    @Id
    @Column(name = "status_id")
    private Long id;

    @Column(name = "status_name")
    private String name;

    @Builder
    public Status(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
