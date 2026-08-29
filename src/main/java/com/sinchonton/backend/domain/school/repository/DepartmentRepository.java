package com.sinchonton.backend.domain.school.repository;

import com.sinchonton.backend.domain.school.entity.Department;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findByCollegeId(Long collegeId);
}
