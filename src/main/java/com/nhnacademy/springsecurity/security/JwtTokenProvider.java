package com.nhnacademy.springsecurity.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtTokenProvider {

    private final String secret;
    private final long validityMs;
    private Key key;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long validityMs
    ){
        this.secret = secret;
        this.validityMs = validityMs;
    }

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication){
        Date now = new Date();
        Date exp = new Date(now.getTime() + validityMs);

        List<String> roles = authentication.getAuthorities().stream()
                .map(Object::toString)
                .toList();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(authentication.getName()) // 로그인한 사용자 이름
                .claim("roles", roles) // 권한 목록
                .setIssuedAt(now) // 발행 시간
                .setExpiration(exp) // 만료시간
                .signWith(key, SignatureAlgorithm.HS256) // HS256 서명
                .compact(); // 문자열 반환
    }

    public boolean validateToken(String token){
        try{
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .setAllowedClockSkewSeconds(60) // 서버간 시계 오차 1분 허용
                    .build()
                    .parseClaimsJws(token); // 서명 & 만료 검사

            // 만료 시간이 현재보다 이전인지 확인
            return  !claims.getBody().getExpiration().before(new Date());
        }catch (JwtException | IllegalArgumentException e){
            log.warn("Invalid or expired JWT : {}", e.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String token){
        // 1. 페이로드 꺼내기
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 2. 사용자 이름 가져오기
        String username = claims.getSubject();

        @SuppressWarnings("unchecked")
                // 3. 권한 문자열을 Spring-Security가 이해하는 GrantedAuthority 객체로 변환
        Collection<SimpleGrantedAuthority> authorities = ((List<String>) claims.get("roles"))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        // 4. principal : 사용자 이름 혹은 UserDetails
        // credentials : 토큰 혹은 null
        // authorities : 권한 목록
        // 추후 이 객체를 SecurityContextHolder에 담아 인가 필터에서 권한 검사에 활용
        return new UsernamePasswordAuthenticationToken(username, token, authorities);
    }
}
