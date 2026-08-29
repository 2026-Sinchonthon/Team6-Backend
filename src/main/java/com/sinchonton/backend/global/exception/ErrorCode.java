package com.sinchonton.backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 서비스 전역 에러 코드.
 *
 * <p>응답의 {@code error.code} 는 enum 이름을 그대로 사용합니다.
 * <pre>
 * { "success": false, "data": null, "error": { "code": "USER_NOT_FOUND", "message": "존재하지 않는 회원입니다." } }
 * </pre>
 *
 * <p>사용법: {@code throw new BusinessException(ErrorCode.USER_NOT_FOUND)}
 *
 * <p>공통 파일이므로 여러 명이 동시에 수정하면 충돌이 납니다.
 * 값을 추가할 때는 <b>자기 도메인 블록 안에서만</b> 추가해 주세요.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ===== 공통 =====
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "대상을 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 요청 방식입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류입니다."),

    // ===== Auth (카카오 OAuth2) =====
    /** 카카오 콜백 실패. 리다이렉트 쿼리의 error=social_login_failed 와 짝을 이룹니다. */
    SOCIAL_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "카카오 로그인에 실패했습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    /** 재발급 요청에 액세스 토큰이 들어왔거나, 서명·만료가 유효하지 않은 경우 */
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),

    // ===== User =====
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    /** 서버가 가진 학교 목록에 없는 값이 들어온 경우 */
    INVALID_SCHOOL(HttpStatus.BAD_REQUEST, "지원하지 않는 학교입니다."),
    /** 서버가 가진 단과대 목록에 없는 값이 들어온 경우 */
    INVALID_COLLEGE(HttpStatus.BAD_REQUEST, "지원하지 않는 단과대입니다."),
    /** 서버가 가진 학과 목록에 없는 값이 들어온 경우 */
    INVALID_DEPARTMENT(HttpStatus.BAD_REQUEST, "지원하지 않는 학과입니다."),
    /** 아직 학교를 선택하지 않은 상태에서 단과대를 먼저 선택하려는 경우 */
    SCHOOL_NOT_SELECTED(HttpStatus.CONFLICT, "학교를 먼저 선택해야 합니다."),
    /** 선택한 단과대가 현재 유저가 선택한 학교 소속이 아닌 경우 */
    COLLEGE_SCHOOL_MISMATCH(HttpStatus.BAD_REQUEST, "선택한 학교에 속하지 않는 단과대입니다."),
    /** 아직 단과대를 선택하지 않은 상태에서 학과를 먼저 입력하려는 경우 */
    COLLEGE_NOT_SELECTED(HttpStatus.CONFLICT, "단과대를 먼저 선택해야 합니다.");

    private final HttpStatus status;
    private final String message;
}
