![img.png](img.png)

## Servlet Container

- **HTTP ↔ Java 객체 변환**
    - 클라이언트로부터 받은 HTTP 요청을 `HttpServletRequest`로, 서블릿이 생성한 결과를 `HttpServletResponse`로 변환
- **서블릿 라이프사이클 관리**
    - 서블릿 인스턴스 생성(`init`) → 요청별 `service(doGet/doPost)` 호출 → 컨테이너 종료 시 `destroy` 호출
- **URL 매핑 및 서블릿 로딩**
    - `web.xml` 또는 어노테이션(`@WebServlet`)을 통해 URL 패턴과 서블릿 클래스를 연결
    - 부하 분산을 위해 멀티스레드 방식으로 요청 처리
- **세션·쿠키·리소스 관리**
    - HTTP 세션 생성·추적(세션 ID, 쿠키)
    - DataSource, JNDI 리소스 풀링 등 컨테이너 차원에서 제공

## Filter

- 모든(또는 특정) 요청·응답을 가로채어 공통 기능 적용

```java
public class MyFilter implements Filter {
    public void init(FilterConfig cfg) { /* 초기화 */ }
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {
        // 전처리: 인코딩, 로깅, 인증 체크 등
        chain.doFilter(req, res);
        // 후처리: 응답 압축, 헤더 추가 등
    }
    public void destroy() { /* 정리 작업 */ }
}
```

**등록과 순서 제어**

- **web.xml**

    ```xml
    <filter>…</filter>
    <filter-mapping>…</filter-mapping>
    ```

- **Spring Boot**

    ```java
    @Component
    @Order(1)
    public class MyFilter implements Filter { ... }
    ```

- **FilterChain**
    - `chain.doFilter()` 호출 전→후로 로직을 분리
    - 순서대로 다음 필터 또는 최종 서블릿 실행

```java
@Override
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws ServletException, IOException {
    // 1) 요청 전처리
    System.out.println("요청 시작: " + ((HttpServletRequest)req).getRequestURI());

    // 2) 다음 필터나 서블릿 호출
    chain.doFilter(req, res);

    // 3) 응답 후처리
    System.out.println("응답 완료: status=" + ((HttpServletResponse)res).getStatus());
}

```

- **대표 사용 사례**
    - 인코딩 설정 (UTF-8 강제)
    - 로깅·트래픽 모니터링
    - 인증·인가 (Spring Security 필터들이 이 패턴으로 동작)
    - CSRF 토큰 검증, CORS 헤더 추가
    - 응답 압축(GZIP), 캐싱 제어