package com.sinchonton.backend.domain.user.dto;

/**
 * 홈 화면 상단에 뜨는 내 소속 요약. school/college/department 는
 * 온보딩을 아직 마치지 않았으면 null 입니다.
 */
public record UserMeSummaryResponse(
        String nickname,
        String profileImage,
        String email,
        String loginProvider,
        Long schoolId,
        String schoolName,
        Long collegeId,
        String collegeName,
        Long departmentId,
        String departmentName
) {
}
