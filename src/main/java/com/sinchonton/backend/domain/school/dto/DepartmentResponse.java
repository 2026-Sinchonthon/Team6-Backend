package com.sinchonton.backend.domain.school.dto;

import com.sinchonton.backend.domain.school.entity.Department;

public record DepartmentResponse(Long id, Long collegeId, String name) {

    public static DepartmentResponse from(Department department) {
        return new DepartmentResponse(department.getId(), department.getCollegeId(), department.getName());
    }
}
