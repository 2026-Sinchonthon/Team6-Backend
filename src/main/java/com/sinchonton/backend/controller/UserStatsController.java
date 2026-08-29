package com.sinchonton.backend.controller;

import com.sinchonton.backend.dto.UserStatsResponse;
import com.sinchonton.backend.service.UserStatsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserStatsController {

    private final UserStatsService userStatsService;

    public UserStatsController(UserStatsService userStatsService) {
        this.userStatsService = userStatsService;
    }

    @GetMapping("/me/stats")
    public UserStatsResponse getMyStats(@RequestParam Long userId) {
        return userStatsService.getStats(userId);
    }
}