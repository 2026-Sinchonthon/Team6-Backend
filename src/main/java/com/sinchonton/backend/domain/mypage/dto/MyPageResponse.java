package com.sinchonton.backend.domain.mypage.dto;

public record MyPageResponse(
        MyPageProfileResponse profile,
        MyPageStudyRecordResponse studyRecord
) {
}
