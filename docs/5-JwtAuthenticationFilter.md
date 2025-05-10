## JwtAuthenticationFilter

```java
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

```

- 헤더 파싱 : Authorization 헤더에서 Bearer 접두사가 있는지 검사
- 이후 validateToekn → 토큰 검사
- 유효하다면 토큰 안에서 인증 정보 추출
- Authentication 객체 SecurityContext에 저장 후 다음 필터로 전달

```java
    // 검사가 필요없는 경로는 검사 예외
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/") || path.startsWith("/h2-console/");
    }

```

- 단 auth/login, auth/register 혹은 h2-console과 같이 검사가 불필요한 부분은 shouldNotFilter 구현으로 검사에서 제외함

필터 개발이 완료되었으므로 SecurityConfig에서 다음과 같이 추가

```java
                // 4) 커스텀 AuthenticationManager 주입
                .authenticationManager(authenticationManager(http))
                // 5) JWT 검증 필터를 UsernamePasswordAuthenticationFilter 앞에 삽입
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
```