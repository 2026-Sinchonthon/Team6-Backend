package com.sinchonton.backend.domain.user.dto;

import jakarta.validation.constraints.NotNull;

public record DepartmentUpdateRequest(

        @NotNull(message = "departmentId가 필요합니다.")
        Long departmentId
) {
}
