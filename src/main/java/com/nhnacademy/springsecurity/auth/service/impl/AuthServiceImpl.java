package com.nhnacademy.springsecurity.auth.service.impl;

import com.nhnacademy.springsecurity.auth.service.AuthService;
import com.nhnacademy.springsecurity.dto.RegisterRequest;
import com.nhnacademy.springsecurity.member.domain.Member;
import com.nhnacademy.springsecurity.member.repository.MemberRepository;
import com.nhnacademy.springsecurity.role.domain.Role;
import com.nhnacademy.springsecurity.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        // 1. 중복 검사
        if(memberRepository.existsByName((request.username()))){
            throw new IllegalArgumentException("이미 사용중인 유저명입니다 : "+ request.username());
        }

        // 2. 기본 ROLE_USER 조회
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(()->
                new IllegalStateException("ROLE_USER가 없습니다"));

        // 3. 엔티티 생성
        Member newMember = Member.create(request, userRole, passwordEncoder);
        memberRepository.save(newMember);


    }
}
