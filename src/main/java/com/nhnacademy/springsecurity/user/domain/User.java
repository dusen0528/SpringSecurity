package com.nhnacademy.springsecurity.user.domain;

import com.nhnacademy.springsecurity.role.domain.Role;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="role_id", nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 기능 미사용 시 항상 true
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금 기능 미사용 시 항상 true
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 자격증명(비밀번호) 만료 기능 미사용 시 항상 true
        return true;
    }

    @Override
    public boolean isEnabled() {
        // DB의 enabled 컬럼 값으로 활성화 여부 결정
        return this.enabled;
    }
}
