package com.sinchonton.backend.service;

import com.sinchonton.backend.dto.CollegeRankingResponse;
import com.sinchonton.backend.dto.DepartmentRankingResponse;
import com.sinchonton.backend.dto.RankingSummaryResponse;
import com.sinchonton.backend.dto.SchoolRankingResponse;
import com.sinchonton.backend.dto.UserRankingResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingService {

    // 홈 화면 상단 랭킹 3칸 — 목업 (입력값 무시하고 고정값 반환)
    public RankingSummaryResponse getRankingSummary(Long userId) {
        return new RankingSummaryResponse(
                new RankingSummaryResponse.SchoolRankEntry(2, 24530),
                new RankingSummaryResponse.CollegeRankEntry(1, 5120),
                new RankingSummaryResponse.MyRankEntry(12, 32)
        );
    }

    // 교내 단과대 랭킹 — 목업
    public List<CollegeRankingResponse> getCollegeRanking(Long schoolId, Long requestingUserId) {
        return List.of(
                new CollegeRankingResponse(1L, "미술대학", 5120L * 3600, 120, 1, true),
                new CollegeRankingResponse(2L, "공과대학", 4800L * 3600, 95, 2, false),
                new CollegeRankingResponse(3L, "경영대학", 3950L * 3600, 82, 3, false),
                new CollegeRankingResponse(4L, "건축도시대학", 3210L * 3600, 65, 4, false),
                new CollegeRankingResponse(5L, "사범대학", 2840L * 3600, 65, 5, false)
        );
    }

    // 미술대학 내 학과 랭킹 — 목업
    public List<DepartmentRankingResponse> getDepartmentRanking(Long collegeId, Long requestingUserId) {
        return List.of(
                new DepartmentRankingResponse(1L, "산업디자인전공", 5120L * 3600, 120, 1, false),
                new DepartmentRankingResponse(2L, "시각디자인전공", 4800L * 3600, 95, 2, true),
                new DepartmentRankingResponse(3L, "동양화과", 3950L * 3600, 82, 3, false),
                new DepartmentRankingResponse(4L, "회화과", 3210L * 3600, 65, 4, false),
                new DepartmentRankingResponse(5L, "섬유미술패션디자인과", 2840L * 3600, 65, 5, false),
                new DepartmentRankingResponse(6L, "조소과", 2840L * 3600, 65, 6, false),
                new DepartmentRankingResponse(7L, "금속조형디자인과", 2840L * 3600, 65, 7, false),
                new DepartmentRankingResponse(8L, "목조형가구학과", 2840L * 3600, 65, 8, false)
        );
    }

    // 개인 랭킹 — 목업 (실제 계산 없이 빈 리스트, 호출부는 null 안전 처리되어 있어 에러 없음)
    public List<UserRankingResponse> getUserRanking(String scope, Long targetId) {
        return List.of();
    }

    // 전체 학교 순위 — 목업
    public List<SchoolRankingResponse> getSchoolRanking() {
        return List.of(
                new SchoolRankingResponse(1L, "홍익대학교", 24530L * 3600, 2)
        );
    }
}