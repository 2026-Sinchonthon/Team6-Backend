package com.sinchonton.backend.domain.mypage.service;

import com.sinchonton.backend.domain.mypage.dto.MyPageProfileResponse;
import com.sinchonton.backend.domain.mypage.dto.MyPageResponse;
import com.sinchonton.backend.domain.mypage.dto.MyPageStudyRecordResponse;
import org.springframework.stereotype.Service;

@Service
public class MyPageService {

    private static final MyPageProfileResponse MOCK_PROFILE = new MyPageProfileResponse(
            "박성찬",
            "박성찬 님",
            "카카오톡",
            "HongikPerson123@kakao.com",
            "홍익대학교",
            "미술대학",
            "시각디자인학과",
            "홍익대학교 미술대학 시각디자인학과",
            "/images/schools/hongik.png"
    );

    private static final MyPageStudyRecordResponse MOCK_STUDY_RECORD = new MyPageStudyRecordResponse(
            "시각디자인학과",
            12,
            "시각디자인학과 내 상위",
            7470L,
            "124h 30m",
            195L,
            "3h 15m"
    );

    public MyPageResponse getMyPage() {
        return new MyPageResponse(MOCK_PROFILE, MOCK_STUDY_RECORD);
    }

    public MyPageProfileResponse getProfile() {
        return MOCK_PROFILE;
    }

    public MyPageStudyRecordResponse getStudyRecord() {
        return MOCK_STUDY_RECORD;
    }
}
