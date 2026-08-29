package com.sinchonton.backend.domain.school.dto;

import jakarta.validation.constraints.NotBlank;

public record SchoolCreateRequest(

        @NotBlank(message = "학교 이름이 필요합니다.")
        String name
) {
}
