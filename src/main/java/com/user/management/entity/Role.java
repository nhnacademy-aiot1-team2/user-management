package com.user.management.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


/**
 * user role entity class
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Entity
@Getter
@Table(name = "user_role")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name")
    private String name;

    /**
     * Instantiates a new Role.
     *
     * @param id   the id
     * @param name the name
     */
    @Builder
    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
