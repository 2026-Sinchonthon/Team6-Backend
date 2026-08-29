package com.sinchonton.backend.controller;

import com.sinchonton.backend.dto.CollegeRankingResponse;
import com.sinchonton.backend.dto.SchoolRankingResponse;
import com.sinchonton.backend.dto.UserRankingResponse;
import com.sinchonton.backend.service.RankingService;
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

    @GetMapping("/colleges")
    public List<CollegeRankingResponse> getCollegeRanking(@RequestParam Long schoolId) {
        return rankingService.getCollegeRanking(schoolId);
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
}