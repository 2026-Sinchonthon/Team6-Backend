package com.sinchonton.backend.controller;

import com.sinchonton.backend.dto.otherschool.OtherSchoolDetailResponse;
import com.sinchonton.backend.dto.otherschool.OtherSchoolSummaryResponse;
import com.sinchonton.backend.global.common.response.ApiResponse;
import com.sinchonton.backend.service.OtherSchoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/other-schools")
@Tag(name = "OtherSchool", description = "타학교 탭에서 사용하는 학교별 총 공부시간 조회 API")
public class OtherSchoolController {

    private final OtherSchoolService otherSchoolService;

    @GetMapping
    @Operation(summary = "타학교 목록 조회", description = "타학교 탭에서 보여줄 학교 이름, 좌표, 총 공부시간, 활성 사용자 수를 조회합니다.")
    public ApiResponse<List<OtherSchoolSummaryResponse>> getOtherSchools() {
        return ApiResponse.success(otherSchoolService.getOtherSchools());
    }

    @GetMapping("/{id}")
    @Operation(summary = "타학교 상세 조회", description = "선택한 타학교의 총 공부시간과 기본 정보를 조회합니다.")
    public ApiResponse<OtherSchoolDetailResponse> getOtherSchool(@PathVariable Long id) {
        return ApiResponse.success(otherSchoolService.getOtherSchool(id));
    }
}
