# Spring Security 

> Spring Security 학습 레포지토리입니다

### 빌드 & 런타임
- Maven 프로젝트, Spring Boot 3.4.4 (Parent)
- Java 21
- 인메모리 H2 데이터베이스 (개발·테스트용)

### dependency
- Spring Web (spring-boot-starter-web)
- Spring Data JPA (spring-boot-starter-data-jpa)
- Spring Security (spring-boot-starter-security)
- JJWT (jjwt-api / jjwt-impl / jjwt-jackson 버전 0.11.5)

### 패키지 구조
```
com.nhnacademy.SpringSecurity
├─ config
│   └─ SecurityConfig.java         # 무상태 JWT 필터 설정
├─ controller
│   └─ AuthController.java         # /api/auth/login 엔드포인트
├─ dto
│   ├─ LoginRequest.java
│   └─ JwtResponse.java
├─ entity
│   └─ User.java                   # id, username, password, roles
├─ repository
│   └─ UserRepository.java
├─ security
│   ├─ JwtTokenProvider.java       # 토큰 생성·검증 로직
│   └─ JwtAuthenticationFilter.java# 요청 헤더 JWT 파싱·인증
└─ service
    └─ CustomUserDetailsService.java # UserDetailsService 구현

```

## docs
- [1. Servlet Filters](docs/Servlet-Filters.md)
