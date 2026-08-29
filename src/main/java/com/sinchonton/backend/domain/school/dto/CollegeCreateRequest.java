package com.sinchonton.backend.domain.school.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CollegeCreateRequest(

        @NotNull(message = "schoolId가 필요합니다.")
        Long schoolId,

        @NotBlank(message = "단과대 이름이 필요합니다.")
        String name
) {
}
