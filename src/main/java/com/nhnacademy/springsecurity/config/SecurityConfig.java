package com.nhnacademy.springsecurity.config;

import com.nhnacademy.springsecurity.user.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailService userDetailService;
//    private final JwtTokenProvider jwtProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1) 세션 비활성화: 무상태 JWT 인증
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 2) H2 콘솔 CSRF 예외 처리 및 iframe 허용
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                ).headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                )
                // 3) URL별 접근 제어
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/api/auth/**").permitAll()  // 공개 경로
                        .anyRequest().authenticated()                                 // 그 외는 인증 필요
                )
                // 4) 커스텀 AuthenticationManager 주입
                .authenticationManager(authenticationManager(http));
                // 5) JWT 검증 필터를 UsernamePasswordAuthenticationFilter 앞에 삽입
//                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider),
//                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        auth
                .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder());

        return auth.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
