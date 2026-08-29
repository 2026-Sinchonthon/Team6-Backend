package com.sinchonton.backend.domain.schoolstat;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolStudyStatRepository extends JpaRepository<SchoolStudyStat, Long> {

    Optional<SchoolStudyStat> findBySchoolId(Long schoolId);
}
