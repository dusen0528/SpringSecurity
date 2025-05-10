package com.nhnacademy.springsecurity.role.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "roles")

@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Getter
public class Role {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Role(String name) {
        this.name = name;
    }
}
