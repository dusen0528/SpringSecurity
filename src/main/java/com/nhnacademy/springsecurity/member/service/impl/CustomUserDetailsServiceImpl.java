package com.nhnacademy.springsecurity.member.service.impl;

import com.nhnacademy.springsecurity.member.repository.MemberRepository;
import com.nhnacademy.springsecurity.member.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
