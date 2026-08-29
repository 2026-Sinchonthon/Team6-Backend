package com.sinchonton.backend.controller;

import com.sinchonton.backend.dto.SchoolOverviewResponse;
import com.sinchonton.backend.global.security.AuthUser;
import com.sinchonton.backend.service.SchoolOverviewService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schools")
public class SchoolOverviewController {

    private final SchoolOverviewService schoolOverviewService;

    public SchoolOverviewController(SchoolOverviewService schoolOverviewService) {
        this.schoolOverviewService = schoolOverviewService;
    }

    @GetMapping("/me/overview")
    public SchoolOverviewResponse getMyOverview(@AuthenticationPrincipal AuthUser authUser) {
        return schoolOverviewService.getMyOverview(authUser.getUserId());
    }
}
