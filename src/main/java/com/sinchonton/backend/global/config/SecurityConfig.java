package com.sinchonton.backend.global.config;

import com.sinchonton.backend.global.security.admin.AdminKeyFilter;
import com.sinchonton.backend.global.security.admin.AdminKeyProperties;
import com.sinchonton.backend.global.security.handler.JwtAccessDeniedHandler;
import com.sinchonton.backend.global.security.handler.JwtAuthenticationEntryPoint;
import com.sinchonton.backend.global.security.jwt.JwtAuthenticationFilter;
import com.sinchonton.backend.global.security.jwt.JwtProperties;
import com.sinchonton.backend.global.security.oauth2.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties({JwtProperties.class, OAuth2Properties.class, AdminKeyProperties.class})
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final OAuth2RedirectUriResolver oAuth2RedirectUriResolver;
    private final AdminKeyFilter adminKeyFilter;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final CorsConfigurationSource corsConfigurationSource;

    /**
     * 인증 없이 접근할 수 있는 경로.
     * 새 경로가 필요하면 여기에 추가하세요.
     */

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/health",
            "/api/auth/**",          // 토큰 재발급
            "/api/admin/**",         // JWT 대신 X-Admin-Key 헤더로 별도 인증 (AdminKeyFilter)
            "/oauth2/**",            // 카카오 로그인 시작 (/oauth2/authorization/kakao)
            "/login/oauth2/**",      // 카카오 콜백 (/login/oauth2/code/kakao)
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/h2-console/**",
            "/api/dev/**"
//            "/api/test/**",          // 임시: 테스트 시더 API (해커톤 개발용, 나중에 제거)
//            "/api/timer/**",         // 임시: 인증 완성 전 개발 편의용
//            "/api/rankings/**",      // 임시: 인증 완성 전 개발 편의용
//            "/api/users/**"          // 임시: 인증 완성 전 개발 편의용
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // JWT 를 쓰므로 세션을 만들지 않습니다. 따라서 CSRF 토큰도 필요 없습니다.
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 스프링 시큐리티 기본 로그인 화면 · 팝업을 끕니다.
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // H2 콘솔이 iframe 을 쓰기 때문에 필요합니다. (local 전용)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()   // CORS preflight
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
                )

                // 로그인 시작 요청의 redirect_uri 를 카카오로 넘어가기 전에 챙겨둡니다.
                .addFilterBefore(new OAuth2RedirectUriCaptureFilter(oAuth2RedirectUriResolver),
                        OAuth2AuthorizationRequestRedirectFilter.class)

                // 카카오 로그인 → CustomOAuth2UserService 로 회원 조회·생성 → 성공 핸들러가 JWT 발급
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint.authorizationRequestResolver(
                                authorizationRequestResolver(clientRegistrationRepository)))
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                )

                .exceptionHandling(handler -> handler
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)   // 401
                        .accessDeniedHandler(jwtAccessDeniedHandler)             // 403
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(adminKeyFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 카카오 인가 요청에 {@code prompt=login} 을 추가해 카카오계정 재인증을 강제합니다.
     *
     * <p>이게 없으면 브라우저에 카카오 로그인 세션이 남아있는 동안은 로그인 화면 없이
     * 바로 통과돼서, 로그인 화면을 확인하거나 다른 계정으로 테스트하기 어렵습니다.
     */
    private OAuth2AuthorizationRequestResolver authorizationRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository) {
        DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository, OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
        resolver.setAuthorizationRequestCustomizer(
                customizer -> customizer.additionalParameters(params -> params.put("prompt", "login")));
        return resolver;
    }
}
