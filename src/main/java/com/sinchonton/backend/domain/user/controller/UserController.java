package com.sinchonton.backend.domain.user.controller;

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

    /** 학교 선택 이후에 학과를 설정합니다. 단과대는 학과로부터 자동으로 결정됩니다. */
    @PatchMapping("/department")
    public ApiResponse<Void> updateDepartment(@AuthenticationPrincipal AuthUser authUser,
                                              @Valid @RequestBody DepartmentUpdateRequest request) {
        userService.updateDepartment(authUser.getUserId(), request.departmentId());
        return ApiResponse.success();
    }
}
