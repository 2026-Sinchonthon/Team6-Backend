package com.sinchonton.backend.domain.school.repository;

import com.sinchonton.backend.domain.school.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}
