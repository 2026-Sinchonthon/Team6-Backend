package com.sinchonton.backend.domain.school.dto;

import com.sinchonton.backend.domain.school.entity.School;

public record SchoolResponse(Long id, String name) {

    public static SchoolResponse from(School school) {
        return new SchoolResponse(school.getId(), school.getName());
    }
}
