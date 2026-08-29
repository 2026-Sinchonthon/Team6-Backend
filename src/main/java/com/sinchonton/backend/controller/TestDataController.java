package com.sinchonton.backend.controller;

import com.sinchonton.backend.entity.User;
import com.sinchonton.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

// 테스트용 임시 컨트롤러. 회원가입 API(A 파트) 완성되면 삭제해도 됨.
@RestController
@RequestMapping("/api/test")
public class TestDataController {

    private final UserRepository userRepository;

    public TestDataController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/seed-users")
    public String seedUsers() {
        User u1 = new User();
        u1.setNickname("철수");
        u1.setSchoolId(1L);
        u1.setCollegeId(1L);
        u1.setDepartmentId(1L);

        User u2 = new User();
        u2.setNickname("영희");
        u2.setSchoolId(1L);
        u2.setCollegeId(1L);
        u2.setDepartmentId(2L);

        User u3 = new User();
        u3.setNickname("민수");
        u3.setSchoolId(1L);
        u3.setCollegeId(2L);
        u3.setDepartmentId(3L);

        User u4 = new User();
        u4.setNickname("지은");
        u4.setSchoolId(1L);
        u4.setCollegeId(2L);
        u4.setDepartmentId(4L);

        userRepository.save(u1);
        userRepository.save(u2);
        userRepository.save(u3);
        userRepository.save(u4);

        return "4명의 테스트 유저 생성 완료 (id: 1~4)";
    }
}