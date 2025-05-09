package com.nhnacademy.springsecurity.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        // 1) "Bearer "로 시작하는 토큰이 있을 때만
        if(header!=null&&header.startsWith("Bearer ")){
            String token = header.substring(7);

            // 2) 토큰이 유효하다면
            if(jwtTokenProvider.validateToken(token)){
                // 3) 토큰에서 Authentication 객체를 가져와
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                // 4) SecurityContext에 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        filterChain.doFilter(request, response);
    }

    // 검사가 필요없는 경로는 검사 예외
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/") || path.startsWith("/h2-console/");
    }


}
