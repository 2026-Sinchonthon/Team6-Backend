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
                user.getEmail(),
                "카카오톡",
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

    /**
     * 단과대는 목록에서 골라 선택합니다. (재)선택 시 이전에 입력했던 학과는 더 이상
     * 유효하지 않으므로 초기화합니다.
     */
    @Transactional
    public void updateCollege(Long userId, Long collegeId) {
        User user = getUser(userId);
        if (user.getSchoolId() == null) {
            throw new BusinessException(ErrorCode.SCHOOL_NOT_SELECTED);
        }

        College college = findCollege(collegeId);
        if (!college.getSchoolId().equals(user.getSchoolId())) {
            throw new BusinessException(ErrorCode.COLLEGE_SCHOOL_MISMATCH);
        }

        user.updateSchool(user.getSchoolId(), college.getId(), null);
    }

    /**
     * 학과는 목록에서 고르지 않고 직접 텍스트로 입력합니다. 이미 선택한 단과대 안에
     * 같은 이름의 학과가 있으면 그걸 쓰고, 없으면 새로 만듭니다.
     */
    @Transactional
    public void updateDepartment(Long userId, String departmentName) {
        User user = getUser(userId);
        if (user.getCollegeId() == null) {
            throw new BusinessException(ErrorCode.COLLEGE_NOT_SELECTED);
        }

        Department department = departmentRepository.findByCollegeIdAndName(user.getCollegeId(), departmentName)
                .orElseGet(() -> departmentRepository.save(new Department(user.getCollegeId(), departmentName)));

        user.updateSchool(user.getSchoolId(), user.getCollegeId(), department.getId());
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
