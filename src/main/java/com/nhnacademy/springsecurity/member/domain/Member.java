package com.nhnacademy.springsecurity.member.domain;

import com.nhnacademy.springsecurity.dto.RegisterRequest;
import com.nhnacademy.springsecurity.role.domain.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="role_id", nullable = false)
    private Role role;

    // 정적 팩토리 메서드
    public static Member create(RegisterRequest req, Role defaultRole, PasswordEncoder encoder) {
        Member m = new Member();
        m.name = req.username();
        m.password = encoder.encode(req.password());
        m.enabled  = true;
        m.role     = defaultRole;
        return m;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return name; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return enabled; }
}
