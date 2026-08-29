package com.sinchonton.backend.domain.user.dto;

import jakarta.validation.constraints.NotNull;

public record CollegeUpdateRequest(

        @NotNull(message = "collegeId가 필요합니다.")
        Long collegeId
) {
}
