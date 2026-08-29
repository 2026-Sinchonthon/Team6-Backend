package com.sinchonton.backend.global.security.admin;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * application.yml 의 admin.key 설정값.
 *
 * @param key 관리자 API({@code /api/admin/**}) 호출 시 {@code X-Admin-Key} 헤더에 넣어야 하는 값.
 *            <b>운영에서는 반드시 환경변수 ADMIN_KEY 로 주입하세요.</b>
 */
@ConfigurationProperties(prefix = "admin")
public record AdminKeyProperties(String key) {
}
