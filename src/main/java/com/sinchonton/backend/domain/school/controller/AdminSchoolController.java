package com.sinchonton.backend.domain.school.controller;

import com.sinchonton.backend.domain.school.dto.CollegeCreateRequest;
import com.sinchonton.backend.domain.school.dto.CollegeResponse;
import com.sinchonton.backend.domain.school.dto.DepartmentCreateRequest;
import com.sinchonton.backend.domain.school.dto.DepartmentResponse;
import com.sinchonton.backend.domain.school.dto.SchoolCreateRequest;
import com.sinchonton.backend.domain.school.dto.SchoolResponse;
import com.sinchonton.backend.domain.school.service.SchoolCatalogService;
import com.sinchonton.backend.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 학교/단과대/학과 등록. 헤더 {@code X-Admin-Key} 필요 (AdminKeyFilter 가 검사).
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminSchoolController {

    private final SchoolCatalogService schoolCatalogService;

    @PostMapping("/schools")
    public ApiResponse<SchoolResponse> createSchool(@Valid @RequestBody SchoolCreateRequest request) {
        return ApiResponse.success(schoolCatalogService.createSchool(request.name()));
    }

    @PostMapping("/colleges")
    public ApiResponse<CollegeResponse> createCollege(@Valid @RequestBody CollegeCreateRequest request) {
        return ApiResponse.success(schoolCatalogService.createCollege(request.schoolId(), request.name()));
    }

    @PostMapping("/departments")
    public ApiResponse<DepartmentResponse> createDepartment(@Valid @RequestBody DepartmentCreateRequest request) {
        return ApiResponse.success(schoolCatalogService.createDepartment(request.collegeId(), request.name()));
    }
}
