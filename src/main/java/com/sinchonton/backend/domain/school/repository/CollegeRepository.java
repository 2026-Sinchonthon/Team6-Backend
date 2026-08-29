package com.sinchonton.backend.domain.school.repository;

import com.sinchonton.backend.domain.school.entity.College;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeRepository extends JpaRepository<College, Long> {
}
