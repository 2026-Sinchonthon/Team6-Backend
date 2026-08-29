package com.sinchonton.backend.service;

import com.sinchonton.backend.domain.school.entity.College;
import com.sinchonton.backend.domain.school.entity.Department;
import com.sinchonton.backend.domain.school.entity.School;
import com.sinchonton.backend.domain.school.repository.CollegeRepository;
import com.sinchonton.backend.domain.school.repository.DepartmentRepository;
import com.sinchonton.backend.domain.school.repository.SchoolRepository;
import com.sinchonton.backend.domain.user.entity.User;
import com.sinchonton.backend.domain.user.repository.UserRepository;
import com.sinchonton.backend.dto.CollegeRankingResponse;
import com.sinchonton.backend.dto.DepartmentRankingResponse;
import com.sinchonton.backend.dto.RankingSummaryResponse;
import com.sinchonton.backend.dto.SchoolRankingResponse;
import com.sinchonton.backend.dto.UserRankingResponse;
import com.sinchonton.backend.global.exception.BusinessException;
import com.sinchonton.backend.global.exception.ErrorCode;
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
    private final CollegeRepository collegeRepository;
    private final DepartmentRepository departmentRepository;

    public RankingService(StudyRecordRepository studyRecordRepository,
                          UserRepository userRepository,
                          SchoolRepository schoolRepository,
                          CollegeRepository collegeRepository,
                          DepartmentRepository departmentRepository) {
        this.studyRecordRepository = studyRecordRepository;
        this.userRepository = userRepository;
        this.schoolRepository = schoolRepository;
        this.collegeRepository = collegeRepository;
        this.departmentRepository = departmentRepository;
    }

    // userId -> 총 공부시간(초) 맵으로 미리 만들어두는 공통 로직
    private Map<Long, Long> getUserIdToSecondsMap() {
        List<UserDurationSum> sums = studyRecordRepository.sumDurationGroupByUser();
        return sums.stream()
                .collect(Collectors.toMap(UserDurationSum::getUserId, UserDurationSum::getTotalSeconds));
    }

    private Set<Long> getActiveUserIds() {
        return new HashSet<>(studyRecordRepository.findActiveUserIds());
    }

    // 단과대별 순위: schoolId로 그 학교 유저들을 collegeId 기준으로 묶어서 합산
    public List<CollegeRankingResponse> getCollegeRanking(Long schoolId, Long requestingUserId) {
        Map<Long, Long> userSeconds = getUserIdToSecondsMap();
        Set<Long> activeUserIds = getActiveUserIds();
        List<User> users = userRepository.findAllBySchoolId(schoolId);

        Map<Long, Long> collegeSeconds = new HashMap<>();
        Map<Long, Integer> collegeActiveCount = new HashMap<>();
        for (User user : users) {
            if (user.getCollegeId() == null) {
                continue;
            }
            Long seconds = userSeconds.getOrDefault(user.getId(), 0L);
            collegeSeconds.merge(user.getCollegeId(), seconds, Long::sum);
            if (activeUserIds.contains(user.getId())) {
                collegeActiveCount.merge(user.getCollegeId(), 1, Integer::sum);
            }
        }

        Long myCollegeId = requestingUserId == null ? null : findUser(requestingUserId).getCollegeId();
        Map<Long, String> collegeNames = collegeRepository.findAllById(collegeSeconds.keySet()).stream()
                .collect(Collectors.toMap(College::getId, College::getName));

        List<CollegeRankingResponse> sorted = collegeSeconds.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .map(e -> new CollegeRankingResponse(
                        e.getKey(),
                        collegeNames.getOrDefault(e.getKey(), "알 수 없음"),
                        e.getValue(),
                        collegeActiveCount.getOrDefault(e.getKey(), 0),
                        0,
                        e.getKey().equals(myCollegeId)
                ))
                .collect(Collectors.toList());

        return assignRanks(sorted, CollegeRankingResponse::getTotalSeconds, (r, rank) -> new CollegeRankingResponse(
                r.getCollegeId(), r.getName(), r.getTotalSeconds(), r.getActiveUserCount(), rank, r.isMine()));
    }

    // 학과별 순위: collegeId로 그 단과대 유저들을 departmentId 기준으로 묶어서 합산
    public List<DepartmentRankingResponse> getDepartmentRanking(Long collegeId, Long requestingUserId) {
        if (!collegeRepository.existsById(collegeId)) {
            throw new BusinessException(ErrorCode.INVALID_COLLEGE);
        }

        Map<Long, Long> userSeconds = getUserIdToSecondsMap();
        Set<Long> activeUserIds = getActiveUserIds();
        List<User> users = userRepository.findAllByCollegeId(collegeId);

        Map<Long, Long> departmentSeconds = new HashMap<>();
        Map<Long, Integer> departmentActiveCount = new HashMap<>();
        for (User user : users) {
            if (user.getDepartmentId() == null) {
                continue;
            }
            Long seconds = userSeconds.getOrDefault(user.getId(), 0L);
            departmentSeconds.merge(user.getDepartmentId(), seconds, Long::sum);
            if (activeUserIds.contains(user.getId())) {
                departmentActiveCount.merge(user.getDepartmentId(), 1, Integer::sum);
            }
        }

        Long myDepartmentId = requestingUserId == null ? null : findUser(requestingUserId).getDepartmentId();
        Map<Long, String> departmentNames = departmentRepository.findAllById(departmentSeconds.keySet()).stream()
                .collect(Collectors.toMap(Department::getId, Department::getName));

        List<DepartmentRankingResponse> sorted = departmentSeconds.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .map(e -> new DepartmentRankingResponse(
                        e.getKey(),
                        departmentNames.getOrDefault(e.getKey(), "알 수 없음"),
                        e.getValue(),
                        departmentActiveCount.getOrDefault(e.getKey(), 0),
                        0,
                        e.getKey().equals(myDepartmentId)
                ))
                .collect(Collectors.toList());

        return assignRanks(sorted, DepartmentRankingResponse::getTotalSeconds, (r, rank) -> new DepartmentRankingResponse(
                r.getDepartmentId(), r.getName(), r.getTotalSeconds(), r.getActiveUserCount(), rank, r.isMine()));
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

    /**
     * 홈 화면 상단 3칸: 신촌 5개 대학 중 우리 학교 순위 / 우리 학교 내 단과대 순위 / 단과대 내 내 순위.
     * 학교·단과대·학과를 전부 선택(온보딩 완료)한 유저만 호출할 수 있습니다.
     */
    public RankingSummaryResponse getRankingSummary(Long userId) {
        User user = findUser(userId);
        if (user.getSchoolId() == null) {
            throw new BusinessException(ErrorCode.SCHOOL_NOT_SELECTED);
        }
        if (user.getCollegeId() == null) {
            throw new BusinessException(ErrorCode.COLLEGE_NOT_SELECTED);
        }

        SchoolRankingResponse mySchool = getSchoolRanking().stream()
                .filter(r -> r.getSchoolId().equals(user.getSchoolId()))
                .findFirst()
                .orElse(null);

        CollegeRankingResponse myCollege = getCollegeRanking(user.getSchoolId(), userId).stream()
                .filter(r -> r.getCollegeId().equals(user.getCollegeId()))
                .findFirst()
                .orElse(null);

        UserRankingResponse myPersonalRank = user.getDepartmentId() == null ? null
                : getUserRanking("department", user.getDepartmentId()).stream()
                        .filter(r -> r.getUserId().equals(userId))
                        .findFirst()
                        .orElse(null);

        return new RankingSummaryResponse(
                new RankingSummaryResponse.SchoolRankEntry(
                        mySchool == null ? 0 : mySchool.getRank(),
                        mySchool == null ? 0 : Math.round(mySchool.getTotalSeconds() / 3600.0)
                ),
                new RankingSummaryResponse.CollegeRankEntry(
                        myCollege == null ? 0 : myCollege.getRank(),
                        myCollege == null ? 0 : Math.round(myCollege.getTotalSeconds() / 3600.0)
                ),
                new RankingSummaryResponse.MyRankEntry(
                        myPersonalRank == null ? 0 : myPersonalRank.getRank(),
                        myPersonalRank == null ? 0 : Math.round(myPersonalRank.getTotalSeconds() / 3600.0)
                )
        );
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    /** 정렬된 리스트에 순위(1부터)를 매겨 새 리스트로 만듭니다. */
    private <T> List<T> assignRanks(List<T> sorted, java.util.function.Function<T, Long> secondsGetter,
                                    java.util.function.BiFunction<T, Integer, T> withRank) {
        List<T> result = new ArrayList<>();
        for (int i = 0; i < sorted.size(); i++) {
            result.add(withRank.apply(sorted.get(i), i + 1));
        }
        return result;
    }
}
