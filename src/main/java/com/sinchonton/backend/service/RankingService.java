package com.sinchonton.backend.service;

import com.sinchonton.backend.domain.school.entity.School;
import com.sinchonton.backend.domain.school.repository.SchoolRepository;
import com.sinchonton.backend.domain.user.entity.User;
import com.sinchonton.backend.domain.user.repository.UserRepository;
import com.sinchonton.backend.dto.CollegeRankingResponse;
import com.sinchonton.backend.dto.SchoolRankingResponse;
import com.sinchonton.backend.dto.UserRankingResponse;
import com.sinchonton.backend.repository.StudyRecordRepository;
import com.sinchonton.backend.repository.StudyRecordRepository.UserDurationSum;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RankingService {

    private final StudyRecordRepository studyRecordRepository;
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;

    public RankingService(StudyRecordRepository studyRecordRepository,
                          UserRepository userRepository,
                          SchoolRepository schoolRepository) {
        this.studyRecordRepository = studyRecordRepository;
        this.userRepository = userRepository;
        this.schoolRepository = schoolRepository;
    }

    // userId -> 총 공부시간(초) 맵으로 미리 만들어두는 공통 로직
    private Map<Long, Long> getUserIdToSecondsMap() {
        List<UserDurationSum> sums = studyRecordRepository.sumDurationGroupByUser();
        return sums.stream()
                .collect(Collectors.toMap(UserDurationSum::getUserId, UserDurationSum::getTotalSeconds));
    }

    // 단과대별 순위: schoolId로 그 학교 유저들을 collegeId 기준으로 묶어서 합산
    public List<CollegeRankingResponse> getCollegeRanking(Long schoolId) {
        Map<Long, Long> userSeconds = getUserIdToSecondsMap();
        List<User> users = userRepository.findAllBySchoolId(schoolId);

        Map<Long, Long> collegeTotal = new HashMap<>();
        for (User user : users) {
            Long seconds = userSeconds.getOrDefault(user.getId(), 0L);
            collegeTotal.merge(user.getCollegeId(), seconds, Long::sum);
        }

        return collegeTotal.entrySet().stream()
                .map(e -> new CollegeRankingResponse(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingLong(CollegeRankingResponse::getTotalSeconds).reversed())
                .collect(Collectors.toList());
    }

    // 개인 랭킹: scope(department/college/school)와 targetId로 범위를 정해서 순위 계산
    public List<UserRankingResponse> getUserRanking(String scope, Long targetId) {
        Map<Long, Long> userSeconds = getUserIdToSecondsMap();

        List<User> users = switch (scope) {
            case "department" -> userRepository.findAllByDepartmentId(targetId);
            case "college" -> userRepository.findAllByCollegeId(targetId);
            case "school" -> userRepository.findAllBySchoolId(targetId);
            default -> throw new IllegalArgumentException("scope는 department, college, school 중 하나여야 합니다.");
        };

        List<UserRankingResponse> sorted = users.stream()
                .map(u -> new UserRankingResponse(
                        u.getId(),
                        u.getNickname(),
                        userSeconds.getOrDefault(u.getId(), 0L),
                        0
                ))
                .sorted(Comparator.comparingLong(UserRankingResponse::getTotalSeconds).reversed())
                .collect(Collectors.toList());

        List<UserRankingResponse> result = new ArrayList<>();
        for (int i = 0; i < sorted.size(); i++) {
            UserRankingResponse r = sorted.get(i);
            result.add(new UserRankingResponse(r.getUserId(), r.getNickname(), r.getTotalSeconds(), i + 1));
        }
        return result;
    }

    // 전체 학교 순위: 필터링 없이 전체 유저를 schoolId 기준으로 묶어서 합산
    public List<SchoolRankingResponse> getSchoolRanking() {
        Map<Long, Long> userSeconds = getUserIdToSecondsMap();
        List<User> allUsers = userRepository.findAll();

        Map<Long, Long> schoolTotal = new HashMap<>();
        for (User user : allUsers) {
            if (user.getSchoolId() == null) {
                continue; // 온보딩 전 유저는 학교 랭킹 집계에서 제외
            }
            Long seconds = userSeconds.getOrDefault(user.getId(), 0L);
            schoolTotal.merge(user.getSchoolId(), seconds, Long::sum);
        }

        // schoolId -> schoolName 매핑
        Map<Long, String> schoolNames = schoolRepository.findAllById(schoolTotal.keySet()).stream()
                .collect(Collectors.toMap(School::getId, School::getName));

        List<SchoolRankingResponse> sorted = schoolTotal.entrySet().stream()
                .map(e -> new SchoolRankingResponse(
                        e.getKey(),
                        schoolNames.getOrDefault(e.getKey(), "알 수 없음"),
                        e.getValue(),
                        0
                ))
                .sorted(Comparator.comparingLong(SchoolRankingResponse::getTotalSeconds).reversed())
                .collect(Collectors.toList());

        List<SchoolRankingResponse> result = new ArrayList<>();
        for (int i = 0; i < sorted.size(); i++) {
            SchoolRankingResponse r = sorted.get(i);
            result.add(new SchoolRankingResponse(r.getSchoolId(), r.getSchoolName(), r.getTotalSeconds(), i + 1));
        }
        return result;
    }
}