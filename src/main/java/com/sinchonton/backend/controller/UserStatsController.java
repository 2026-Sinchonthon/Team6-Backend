package com.sinchonton.backend.controller;

import com.sinchonton.backend.dto.UserStatsResponse;
import com.sinchonton.backend.global.security.AuthUser;
import com.sinchonton.backend.service.UserStatsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserStatsController {

    private final UserStatsService userStatsService;

    public UserStatsController(UserStatsService userStatsService) {
        this.userStatsService = userStatsService;
    }

    @GetMapping("/me/stats")
    public UserStatsResponse getMyStats(@AuthenticationPrincipal AuthUser authUser) {
        return userStatsService.getStats(authUser.getUserId());
    }
}