package com.nhnacademy.springsecurity.role.repository;

import com.nhnacademy.springsecurity.role.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
