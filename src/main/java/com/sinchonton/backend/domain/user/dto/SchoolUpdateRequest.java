package com.sinchonton.backend.domain.user.dto;

import jakarta.validation.constraints.NotNull;

public record SchoolUpdateRequest(

        @NotNull(message = "schoolId가 필요합니다.")
        Long schoolId
) {
}
