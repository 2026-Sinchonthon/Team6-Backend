package com.sinchonton.backend.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger UI: /swagger-ui.html
 *
 * <p>우측 상단 <b>Authorize</b> 버튼에 액세스 토큰을 넣으면 이후 요청에 자동으로
 * {@code Authorization: Bearer ...} 헤더가 붙습니다. ("Bearer " 는 빼고 토큰만 붙여넣으세요.)
 */
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("신촌톤 6팀 API")
                .version("v1")
                .description("""
                        열품타 스타일 스터디 타임 트래킹 서비스 API 문서입니다.

                        - 모든 응답은 `{ "success": ..., "data": ..., "error": ... }` 형식입니다.
                        - 로그인은 GET /oauth2/authorization/kakao 로 시작합니다.
                        - 그 외 대부분의 API 는 인증이 필요합니다.
                        """);

        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        return new OpenAPI()
                .info(info)
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, bearerScheme));
    }
}
