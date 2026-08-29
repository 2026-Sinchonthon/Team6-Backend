package com.sinchonton.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRankingResponse {
    private Long userId;
    private String nickname;
    private Long totalSeconds;
    private int rank; // 몇 등인지
}