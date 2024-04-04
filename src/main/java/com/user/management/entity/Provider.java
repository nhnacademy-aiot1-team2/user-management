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
@Table(name = "user_provider")
public class Provider {
    @Id
    @Column(name = "provider_id")
    private String id;

    @Column(name = "provider_name")
    private String name;

    @Builder
    public Provider(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
