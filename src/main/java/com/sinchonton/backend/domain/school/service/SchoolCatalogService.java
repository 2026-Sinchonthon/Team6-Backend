package com.sinchonton.backend.domain.school.service;

import com.sinchonton.backend.domain.school.dto.CollegeResponse;
import com.sinchonton.backend.domain.school.dto.DepartmentResponse;
import com.sinchonton.backend.domain.school.dto.SchoolResponse;
import com.sinchonton.backend.domain.school.entity.College;
import com.sinchonton.backend.domain.school.entity.Department;
import com.sinchonton.backend.domain.school.entity.School;
import com.sinchonton.backend.domain.school.repository.CollegeRepository;
import com.sinchonton.backend.domain.school.repository.DepartmentRepository;
import com.sinchonton.backend.domain.school.repository.SchoolRepository;
import com.sinchonton.backend.global.exception.BusinessException;
import com.sinchonton.backend.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 학교/단과대/학과 조회 + 관리자용 등록.
 *
 * <p>온보딩 화면(학교 인증, 단과대·학과 선택)에서 드롭다운을 채우는 데 씁니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchoolCatalogService {

    private final SchoolRepository schoolRepository;
    private final CollegeRepository collegeRepository;
    private final DepartmentRepository departmentRepository;

    public List<SchoolResponse> getSchools() {
        return schoolRepository.findAll().stream().map(SchoolResponse::from).toList();
    }

    public List<CollegeResponse> getColleges(Long schoolId) {
        if (!schoolRepository.existsById(schoolId)) {
            throw new BusinessException(ErrorCode.INVALID_SCHOOL);
        }
        return collegeRepository.findBySchoolId(schoolId).stream().map(CollegeResponse::from).toList();
    }

    public List<DepartmentResponse> getDepartments(Long collegeId) {
        if (!collegeRepository.existsById(collegeId)) {
            throw new BusinessException(ErrorCode.INVALID_COLLEGE);
        }
        return departmentRepository.findByCollegeId(collegeId).stream().map(DepartmentResponse::from).toList();
    }

    @Transactional
    public SchoolResponse createSchool(String name) {
        School saved = schoolRepository.findByName(name)
                .orElseGet(() -> schoolRepository.save(new School(name)));
        return SchoolResponse.from(saved);
    }

    @Transactional
    public CollegeResponse createCollege(Long schoolId, String name) {
        if (!schoolRepository.existsById(schoolId)) {
            throw new BusinessException(ErrorCode.INVALID_SCHOOL);
        }
        College saved = collegeRepository.findBySchoolIdAndName(schoolId, name)
                .orElseGet(() -> collegeRepository.save(new College(schoolId, name)));
        return CollegeResponse.from(saved);
    }

    @Transactional
    public DepartmentResponse createDepartment(Long collegeId, String name) {
        if (!collegeRepository.existsById(collegeId)) {
            throw new BusinessException(ErrorCode.INVALID_COLLEGE);
        }
        Department saved = departmentRepository.findByCollegeIdAndName(collegeId, name)
                .orElseGet(() -> departmentRepository.save(new Department(collegeId, name)));
        return DepartmentResponse.from(saved);
    }
}
