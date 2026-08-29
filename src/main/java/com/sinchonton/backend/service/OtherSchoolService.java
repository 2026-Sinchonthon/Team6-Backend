package com.sinchonton.backend.service;

import com.sinchonton.backend.domain.school.entity.School;
import com.sinchonton.backend.domain.school.repository.SchoolRepository;
import com.sinchonton.backend.domain.schoolstat.SchoolStudyStat;
import com.sinchonton.backend.domain.schoolstat.SchoolStudyStatRepository;
import com.sinchonton.backend.dto.otherschool.OtherSchoolDetailResponse;
import com.sinchonton.backend.dto.otherschool.OtherSchoolSummaryResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class OtherSchoolService {

    private final SchoolRepository schoolRepository;
    private final SchoolStudyStatRepository schoolStudyStatRepository;

    public List<OtherSchoolSummaryResponse> getOtherSchools() {
        return schoolStudyStatRepository.findAll()
                .stream()
                .map(stat -> OtherSchoolSummaryResponse.from(getSchool(stat.getSchoolId()), stat))
                .toList();
    }

    public OtherSchoolDetailResponse getOtherSchool(Long schoolId) {
        School school = getSchool(schoolId);
        SchoolStudyStat stat = schoolStudyStatRepository.findBySchoolId(schoolId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "School study stat not found"));
        return OtherSchoolDetailResponse.from(school, stat);
    }

    private School getSchool(Long schoolId) {
        return schoolRepository.findById(schoolId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "School not found"));
    }
}
