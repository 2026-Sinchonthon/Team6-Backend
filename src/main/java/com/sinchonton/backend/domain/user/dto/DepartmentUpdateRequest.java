package com.sinchonton.backend.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record DepartmentUpdateRequest(

        @NotBlank(message = "학과 이름이 필요합니다.")
        String name
) {
}
