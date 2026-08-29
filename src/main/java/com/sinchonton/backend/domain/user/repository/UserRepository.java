package com.sinchonton.backend.domain.user.repository;

import com.sinchonton.backend.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySocialId(String socialId);

    List<User> findAllBySchoolId(Long schoolId);
    List<User> findAllByCollegeId(Long collegeId);
    List<User> findAllByDepartmentId(Long departmentId);
}
