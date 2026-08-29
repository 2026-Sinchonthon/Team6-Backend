package com.sinchonton.backend.global.security.admin;

import com.sinchonton.backend.global.common.response.ApiResponse;
import com.sinchonton.backend.global.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

/**
 * {@code /api/admin/**} 요청은 로그인 JWT 대신 {@code X-Admin-Key} 헤더로 막습니다.
 * 유저 role 체계를 새로 만들기엔 부담스러운 해커톤 규모라 가볍게 처리합니다.
 */
@Component
@RequiredArgsConstructor
public class AdminKeyFilter extends OncePerRequestFilter {

    private static final String ADMIN_KEY_HEADER = "X-Admin-Key";
    private static final String ADMIN_PATH_PREFIX = "/api/admin/";

    private final AdminKeyProperties adminKeyProperties;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (!request.getRequestURI().startsWith(ADMIN_PATH_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String providedKey = request.getHeader(ADMIN_KEY_HEADER);
        String configuredKey = adminKeyProperties.key();

        if (configuredKey == null || configuredKey.isBlank() || !configuredKey.equals(providedKey)) {
            response.setStatus(ErrorCode.FORBIDDEN.getStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            objectMapper.writeValue(response.getWriter(), ApiResponse.error(ErrorCode.FORBIDDEN));
            return;
        }

        filterChain.doFilter(request, response);
    }
}
