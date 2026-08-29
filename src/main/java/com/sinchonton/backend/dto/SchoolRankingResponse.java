package com.sinchonton.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SchoolRankingResponse {
    private Long schoolId;
    private Long totalSeconds;
    private int rank; // 몇 위인지 (프론트에서 "우리 학교 3위" 표시할 때 바로 쓸 수 있게)
}