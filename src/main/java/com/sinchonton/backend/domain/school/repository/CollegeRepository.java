package com.sinchonton.backend.domain.school.repository;

import com.sinchonton.backend.domain.school.entity.College;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeRepository extends JpaRepository<College, Long> {

    List<College> findBySchoolId(Long schoolId);

    Optional<College> findBySchoolIdAndName(Long schoolId, String name);
}
