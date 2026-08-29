package com.sinchonton.backend.domain.mypage.controller;

import com.sinchonton.backend.domain.mypage.dto.MyPageProfileResponse;
import com.sinchonton.backend.domain.mypage.dto.MyPageResponse;
import com.sinchonton.backend.domain.mypage.dto.MyPageStudyRecordResponse;
import com.sinchonton.backend.domain.mypage.service.MyPageService;
import com.sinchonton.backend.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
@Tag(name = "MyPage", description = "마이페이지 화면에서 사용하는 프로필/스터디 기록 목업 조회 API")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping
    @Operation(summary = "마이페이지 전체 조회", description = "마이페이지 프로필과 스터디 기록 목업 데이터를 한 번에 조회합니다.")
    public ApiResponse<MyPageResponse> getMyPage() {
        return ApiResponse.success(myPageService.getMyPage());
    }

    @GetMapping("/profile")
    @Operation(summary = "마이페이지 프로필 조회", description = "마이페이지 상단 프로필과 계정 관리 화면에 사용할 목업 데이터를 조회합니다.")
    public ApiResponse<MyPageProfileResponse> getProfile() {
        return ApiResponse.success(myPageService.getProfile());
    }

    @GetMapping("/study-record")
    @Operation(summary = "마이페이지 스터디 기록 조회", description = "마이페이지 나의 스터디 기록 영역에 사용할 목업 데이터를 조회합니다.")
    public ApiResponse<MyPageStudyRecordResponse> getStudyRecord() {
        return ApiResponse.success(myPageService.getStudyRecord());
    }
}
