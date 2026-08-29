package com.sinchonton.backend.domain.user.controller;

import com.sinchonton.backend.domain.user.dto.CollegeUpdateRequest;
import com.sinchonton.backend.domain.user.dto.DepartmentUpdateRequest;
import com.sinchonton.backend.domain.user.dto.SchoolUpdateRequest;
import com.sinchonton.backend.domain.user.dto.UserMeSummaryResponse;
import com.sinchonton.backend.domain.user.service.UserService;
import com.sinchonton.backend.global.common.response.ApiResponse;
import com.sinchonton.backend.global.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** 홈 화면 상단에 뜨는 내 소속 요약. 온보딩 전이면 school/college/department 가 null. */
    @GetMapping("/summary")
    public ApiResponse<UserMeSummaryResponse> summary(@AuthenticationPrincipal AuthUser authUser) {
        return ApiResponse.success(userService.getSummary(authUser.getUserId()));
    }

    /** 온보딩 · 재선택 시 학교를 설정합니다. 단과대 · 학과 선택은 초기화됩니다. */
    @PatchMapping("/school")
    public ApiResponse<Void> updateSchool(@AuthenticationPrincipal AuthUser authUser,
                                          @Valid @RequestBody SchoolUpdateRequest request) {
        userService.updateSchool(authUser.getUserId(), request.schoolId());
        return ApiResponse.success();
    }

    /** 학교 선택 이후에 단과대를 설정합니다 (목록에서 선택). 재선택 시 학과는 초기화됩니다. */
    @PatchMapping("/college")
    public ApiResponse<Void> updateCollege(@AuthenticationPrincipal AuthUser authUser,
                                           @Valid @RequestBody CollegeUpdateRequest request) {
        userService.updateCollege(authUser.getUserId(), request.collegeId());
        return ApiResponse.success();
    }

    /**
     * 단과대 선택 이후에 학과를 설정합니다. 목록에서 고르지 않고 텍스트로 직접
     * 입력받습니다 — 이미 있는 이름이면 그 학과를, 없으면 새로 만들어서 씁니다.
     */
    @PatchMapping("/department")
    public ApiResponse<Void> updateDepartment(@AuthenticationPrincipal AuthUser authUser,
                                              @Valid @RequestBody DepartmentUpdateRequest request) {
        userService.updateDepartment(authUser.getUserId(), request.name());
        return ApiResponse.success();
    }
}
