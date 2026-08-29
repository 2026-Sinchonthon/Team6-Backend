package com.sinchonton.backend.domain.school.dto;

import com.sinchonton.backend.domain.school.entity.College;

public record CollegeResponse(Long id, Long schoolId, String name) {

    public static CollegeResponse from(College college) {
        return new CollegeResponse(college.getId(), college.getSchoolId(), college.getName());
    }
}
