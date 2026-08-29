package com.sinchonton.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CollegeRankingResponse {
    private Long collegeId;
    private Long totalSeconds;
}