package com.sinchonton.backend.controller;

import com.sinchonton.backend.domain.user.entity.User;
import com.sinchonton.backend.domain.user.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestDataController {

    private final UserRepository userRepository;

    public TestDataController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/seed-users")
    public String seedUsers() {
        User u1 = User.create("test1@test.com", "social-1", "철수", null);
        u1.updateSchool(1L, 1L, 1L);

        User u2 = User.create("test2@test.com", "social-2", "영희", null);
        u2.updateSchool(1L, 1L, 2L);

        User u3 = User.create("test3@test.com", "social-3", "민수", null);
        u3.updateSchool(1L, 2L, 3L);

        User u4 = User.create("test4@test.com", "social-4", "지은", null);
        u4.updateSchool(1L, 2L, 4L);

        userRepository.save(u1);
        userRepository.save(u2);
        userRepository.save(u3);
        userRepository.save(u4);

        return "4명의 테스트 유저 생성 완료 (id: 1~4)";
    }
}