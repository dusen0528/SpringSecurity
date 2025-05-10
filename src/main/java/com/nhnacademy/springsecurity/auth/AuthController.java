package com.nhnacademy.springsecurity.auth;

import com.nhnacademy.springsecurity.auth.service.AuthService;
import com.nhnacademy.springsecurity.dto.JwtResponse;
import com.nhnacademy.springsecurity.dto.LoginRequest;
import com.nhnacademy.springsecurity.dto.RegisterRequest;
import com.nhnacademy.springsecurity.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest req){
        // 1) 아이디/비밀번호 인증 시도
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );

        // 2) 인증 성공시 JWT 생성
        String token = jwtTokenProvider.generateToken(auth);

        // 3) 클라이언트에 토큰 반환
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody RegisterRequest req){
        authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
