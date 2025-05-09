package com.nhnacademy.springsecurity.user.service.impl;

import com.nhnacademy.springsecurity.user.repository.UserRepository;
import com.nhnacademy.springsecurity.user.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailServiceImpl implements CustomUserDetailService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("user not found : " + username));
    }
}
