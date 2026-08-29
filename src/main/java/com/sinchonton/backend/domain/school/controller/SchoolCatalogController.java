package com.sinchonton.backend.domain.school.controller;

import com.sinchonton.backend.domain.school.dto.CollegeResponse;
import com.sinchonton.backend.domain.school.dto.DepartmentResponse;
import com.sinchonton.backend.domain.school.dto.SchoolResponse;
import com.sinchonton.backend.domain.school.service.SchoolCatalogService;
import com.sinchonton.backend.global.common.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 온보딩 드롭다운용 조회 API. 로그인한 유저면 누구나 볼 수 있습니다.
 */
@RestController
@RequiredArgsConstructor
public class SchoolCatalogController {

    private final SchoolCatalogService schoolCatalogService;

    @GetMapping("/api/schools")
    public ApiResponse<List<SchoolResponse>> getSchools() {
        return ApiResponse.success(schoolCatalogService.getSchools());
    }

    @GetMapping("/api/schools/{schoolId}/colleges")
    public ApiResponse<List<CollegeResponse>> getColleges(@PathVariable Long schoolId) {
        return ApiResponse.success(schoolCatalogService.getColleges(schoolId));
    }

    @GetMapping("/api/colleges/{collegeId}/departments")
    public ApiResponse<List<DepartmentResponse>> getDepartments(@PathVariable Long collegeId) {
        return ApiResponse.success(schoolCatalogService.getDepartments(collegeId));
    }
}
