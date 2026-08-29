package com.sinchonton.backend.domain.school.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DepartmentCreateRequest(

        @NotNull(message = "collegeId가 필요합니다.")
        Long collegeId,

        @NotBlank(message = "학과 이름이 필요합니다.")
        String name
) {
}
