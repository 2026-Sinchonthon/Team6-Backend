package com.sinchonton.backend.domain.school.config;

import com.sinchonton.backend.domain.school.entity.College;
import com.sinchonton.backend.domain.school.entity.Department;
import com.sinchonton.backend.domain.school.entity.School;
import com.sinchonton.backend.domain.school.repository.CollegeRepository;
import com.sinchonton.backend.domain.school.repository.DepartmentRepository;
import com.sinchonton.backend.domain.school.repository.SchoolRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 서버가 뜰 때 학교/단과대/학과 기본 데이터가 비어 있으면 채워 넣습니다.
 * 온보딩 화면(학교·단과대·학과 선택)을 바로 테스트할 수 있게 하기 위한 것으로,
 * 전체 목록이 아니라 학교당 대표 단과대·학과 몇 개만 넣습니다.
 * 나머지는 {@code POST /api/admin/*} 로 추가하세요.
 */
@Configuration
@RequiredArgsConstructor
public class SchoolCatalogInitializer {

    private final SchoolRepository schoolRepository;
    private final CollegeRepository collegeRepository;
    private final DepartmentRepository departmentRepository;

    /** 학교 이름 → (단과대 이름 → 학과 이름 목록) */
    private static final Map<String, Map<String, List<String>>> CATALOG = Map.of(
            "연세대학교", Map.of(
                    "문과대학", List.of("국어국문학과", "사학과"),
                    "공과대학", List.of("컴퓨터과학과", "전기전자공학과"),
                    "상경대학", List.of("경제학과", "응용통계학과")
            ),
            "서강대학교", Map.of(
                    "인문대학", List.of("국어국문학과", "사학과"),
                    "공과대학", List.of("컴퓨터공학과", "전자공학과"),
                    "경영대학", List.of("경영학과")
            ),
            "이화여자대학교", Map.of(
                    "인문과학대학", List.of("국어국문학과", "사학과"),
                    "공과대학", List.of("컴퓨터공학과"),
                    "사회과학대학", List.of("정치외교학과", "행정학과")
            ),
            "명지대학교", Map.of(
                    "인문대학", List.of("국어국문학과"),
                    "공과대학", List.of("컴퓨터공학과", "전기공학과")
            ),
            "홍익대학교", Map.of(
                    "공과대학", List.of("컴퓨터공학과", "기계시스템디자인공학과"),
                    "미술대학", List.of("회화과", "시각디자인과")
            )
    );

    @Bean
    CommandLineRunner initializeSchoolCatalog() {
        return args -> CATALOG.forEach((schoolName, colleges) -> {
            School school = getOrCreateSchool(schoolName);
            colleges.forEach((collegeName, departments) -> {
                College college = getOrCreateCollege(school.getId(), collegeName);
                departments.forEach(departmentName -> getOrCreateDepartment(college.getId(), departmentName));
            });
        });
    }

    private School getOrCreateSchool(String name) {
        return schoolRepository.findByName(name)
                .orElseGet(() -> schoolRepository.save(new School(name)));
    }

    private College getOrCreateCollege(Long schoolId, String name) {
        return collegeRepository.findBySchoolId(schoolId).stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElseGet(() -> collegeRepository.save(new College(schoolId, name)));
    }

    private void getOrCreateDepartment(Long collegeId, String name) {
        boolean exists = departmentRepository.findByCollegeId(collegeId).stream()
                .anyMatch(d -> d.getName().equals(name));
        if (!exists) {
            departmentRepository.save(new Department(collegeId, name));
        }
    }
}
