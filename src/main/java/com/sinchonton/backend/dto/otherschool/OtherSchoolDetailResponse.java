package com.sinchonton.backend.dto.otherschool;

import com.sinchonton.backend.domain.school.entity.School;
import com.sinchonton.backend.domain.schoolstat.SchoolStudyStat;

public record OtherSchoolDetailResponse(
        Long schoolId,
        String schoolName,
        Double latitude,
        Double longitude,
        Long totalStudyMinutes,
        Integer activeUserCount
) {

    public static OtherSchoolDetailResponse from(School school, SchoolStudyStat stat) {
        return new OtherSchoolDetailResponse(
                school.getId(),
                school.getName(),
                stat.getLatitude(),
                stat.getLongitude(),
                stat.getTotalStudyMinutes(),
                stat.getActiveUserCount()
        );
    }
}
