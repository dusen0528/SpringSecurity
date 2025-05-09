## Spring Security JWT 방식 기본 동작 흐름 (무상태)

1. **클라이언트 로그인 요청**
    - `POST /api/auth/login`
    - 요청 본문에 `{ "username": "...", "password": "..." }` 포함
2. **UsernamePasswordAuthenticationFilter 실행**
    - HTTP 요청 가로채 username/password 추출
    - `UsernamePasswordAuthenticationToken(username, password, false)` 생성
    - `AuthenticationManager.authenticate()` 호출
3. **AuthenticationManager → DaoAuthenticationProvider**
    - `supports()` 체크 후 `DaoAuthenticationProvider.authenticate()` 위임
    - `UserDetailsService.loadUserByUsername()` → `UserDetails` 로드
    - `PasswordEncoder.matches()`로 비밀번호 검증
    - 성공 시 `isAuthenticated=true`인 토큰 반환
4. **JWT 토큰 생성 및 응답**

    ```java
    String token = jwtProvider.generateToken(authResult);
    response.addHeader("Authorization", "Bearer " + token)
    ```

    - `jwtProvider`가 사용자 정보(`UserDetails`)로 JWT 페이로드 작성
    - 비밀키로 서명된 JWT를 응답 헤더 또는 본문에 담아 클라이언트에 전송
5. **클라이언트 JWT 저장**
    - 브라우저: `localStorage` 또는 `sessionStorage`
    - 모바일 앱: 안전한 스토리지
6. **클라이언트의 모든 후속 요청**
    - `Authorization: Bearer <JWT>` 헤더 포함
7. **JwtAuthenticationFilter 실행**
    - 요청 가로채 `Authorization` 헤더에서 토큰 추출
    - `jwtProvider.validateToken(token)`로 서명·만료 검사
    - 유효 시 `UsernamePasswordAuthenticationToken(principal, null, authorities)` 생성(`isAuthenticated=true`)
8. **SecurityContextHolder에 저장 (무상태 요청 범위)**

    ```java
    SecurityContextHolder.getContext().setAuthentication(authToken);
    ```

    - 세션 없이, **요청 처리 중에만** `SecurityContextHolder`에 인증 정보 보관
9. **FilterSecurityInterceptor 인가 처리**
    - `SecurityMetadataSource`로 URL·권한 매핑 조회
    - `AccessDecisionManager`가 `Authentication.getAuthorities()`와 대조해 접근 허용 여부 결정
10. **컨트롤러 실행 → 응답 반환**
    - `DispatcherServlet`이 컨트롤러 호출
    - 결과를 JSON 등으로 반환
    - **세션 미사용**: 모든 인증 정보가 JWT 기반으로 처리되므로 서버에 상태 없음

---

### HttpSecurity 설정 예시

```java
http
  .csrf().disable()
  .sessionManagement()
    .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
  .authorizeRequests()
    .antMatchers("/api/auth/**").permitAll()
    .anyRequest().authenticated().and()
  .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
```

- **`STATELESS`**: 세션 생성·조회 비활성
- **`jwtAuthenticationFilter`**: 커스텀 JWT 검증 필터를 기본 인증 필터 앞에 배치