package com.sinchonton.backend.controller;

import com.sinchonton.backend.global.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

// 로컬 개발 전용. local 프로필에서만 활성화되므로 배포 환경(prod)에는 아예 존재하지 않음.
// 카카오 로그인 없이 바로 JWT를 발급받아 테스트할 수 있게 해주는 임시 API.
@Profile("local")
@RestController
@RequestMapping("/api/dev")
public class DevAuthController {

    private final JwtTokenProvider jwtTokenProvider;

    public DevAuthController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/token")
    public String issueToken(@RequestParam Long userId) {
        return jwtTokenProvider.createAccessToken(userId);
    }
}