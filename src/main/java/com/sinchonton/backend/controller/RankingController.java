package com.sinchonton.backend.controller;

import com.sinchonton.backend.dto.CollegeRankingResponse;
import com.sinchonton.backend.dto.DepartmentRankingResponse;
import com.sinchonton.backend.dto.RankingSummaryResponse;
import com.sinchonton.backend.dto.SchoolRankingResponse;
import com.sinchonton.backend.dto.UserRankingResponse;
import com.sinchonton.backend.global.security.AuthUser;
import com.sinchonton.backend.service.RankingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rankings")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    // 지금은 SecurityConfig에서 /api/rankings/** 가 인증 필수라 authUser가 null일 일이
    // 없다. 나중에 이 경로를 공개로 풀 경우를 대비해 null-safe 하게 짜뒀다 — isMine
    // 계산용 유저 식별 없이도 순위 목록 자체는 볼 수 있게.
    @GetMapping("/colleges")
    public List<CollegeRankingResponse> getCollegeRanking(@RequestParam Long schoolId,
                                                          @AuthenticationPrincipal AuthUser authUser) {
        return rankingService.getCollegeRanking(schoolId, authUser == null ? null : authUser.getUserId());
    }

    @GetMapping("/departments")
    public List<DepartmentRankingResponse> getDepartmentRanking(@RequestParam Long collegeId,
                                                                @AuthenticationPrincipal AuthUser authUser) {
        return rankingService.getDepartmentRanking(collegeId, authUser == null ? null : authUser.getUserId());
    }

    @GetMapping("/users")
    public List<UserRankingResponse> getUserRanking(
            @RequestParam String scope,
            @RequestParam Long targetId
    ) {
        return rankingService.getUserRanking(scope, targetId);
    }

    @GetMapping("/schools")
    public List<SchoolRankingResponse> getSchoolRanking() {
        return rankingService.getSchoolRanking();
    }

    /** 홈 화면 상단 3칸(우리 학교 순위 / 우리 단과대 순위 / 내 순위) 요약. */
    @GetMapping("/summary")
    public RankingSummaryResponse getRankingSummary(@AuthenticationPrincipal AuthUser authUser) {
        return rankingService.getRankingSummary(authUser.getUserId());
    }
}
