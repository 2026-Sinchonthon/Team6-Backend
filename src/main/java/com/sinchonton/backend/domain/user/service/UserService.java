package com.sinchonton.backend.domain.user.service;

import com.sinchonton.backend.domain.school.entity.College;
import com.sinchonton.backend.domain.school.entity.Department;
import com.sinchonton.backend.domain.school.entity.School;
import com.sinchonton.backend.domain.school.repository.CollegeRepository;
import com.sinchonton.backend.domain.school.repository.DepartmentRepository;
import com.sinchonton.backend.domain.school.repository.SchoolRepository;
import com.sinchonton.backend.domain.user.dto.UserMeSummaryResponse;
import com.sinchonton.backend.domain.user.entity.User;
import com.sinchonton.backend.domain.user.repository.UserRepository;
import com.sinchonton.backend.global.exception.BusinessException;
import com.sinchonton.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final CollegeRepository collegeRepository;
    private final DepartmentRepository departmentRepository;

    public UserMeSummaryResponse getSummary(Long userId) {
        User user = getUser(userId);

        String schoolName = user.getSchoolId() == null ? null : findSchool(user.getSchoolId()).getName();
        String collegeName = user.getCollegeId() == null ? null : findCollege(user.getCollegeId()).getName();
        String departmentName = user.getDepartmentId() == null ? null : findDepartment(user.getDepartmentId()).getName();

        return new UserMeSummaryResponse(
                user.getNickname(),
                user.getProfileImage(),
                user.getSchoolId(),
                schoolName,
                user.getCollegeId(),
                collegeName,
                user.getDepartmentId(),
                departmentName
        );
    }

    /** 학교를 (다시) 선택하면 이전에 골랐던 단과대 · 학과는 더 이상 유효하지 않으므로 함께 초기화합니다. */
    @Transactional
    public void updateSchool(Long userId, Long schoolId) {
        User user = getUser(userId);
        findSchool(schoolId);

        user.updateSchool(schoolId, null, null);
    }

    @Transactional
    public void updateDepartment(Long userId, Long departmentId) {
        User user = getUser(userId);
        if (user.getSchoolId() == null) {
            throw new BusinessException(ErrorCode.SCHOOL_NOT_SELECTED);
        }

        Department department = findDepartment(departmentId);
        College college = findCollege(department.getCollegeId());
        if (!college.getSchoolId().equals(user.getSchoolId())) {
            throw new BusinessException(ErrorCode.DEPARTMENT_SCHOOL_MISMATCH);
        }

        user.updateSchool(user.getSchoolId(), college.getId(), department.getId());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    private School findSchool(Long schoolId) {
        return schoolRepository.findById(schoolId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_SCHOOL));
    }

    private College findCollege(Long collegeId) {
        return collegeRepository.findById(collegeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_COLLEGE));
    }

    private Department findDepartment(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_DEPARTMENT));
    }
}
