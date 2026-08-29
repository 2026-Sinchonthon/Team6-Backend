package com.sinchonton.backend.domain.schoolstat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "school_study_stats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SchoolStudyStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long schoolId;

    private Double latitude;

    private Double longitude;

    private Long totalStudyMinutes;

    private Integer activeUserCount;

    public SchoolStudyStat(
            Long schoolId,
            Double latitude,
            Double longitude,
            Long totalStudyMinutes,
            Integer activeUserCount
    ) {
        this.schoolId = schoolId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.totalStudyMinutes = totalStudyMinutes;
        this.activeUserCount = activeUserCount;
    }
}
