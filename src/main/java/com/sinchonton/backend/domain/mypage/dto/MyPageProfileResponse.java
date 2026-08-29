package com.sinchonton.backend.domain.mypage.dto;

public record MyPageProfileResponse(
        String name,
        String displayName,
        String loginProvider,
        String loginEmail,
        String schoolName,
        String collegeName,
        String departmentName,
        String affiliation,
        String profileImageUrl
) {
}
