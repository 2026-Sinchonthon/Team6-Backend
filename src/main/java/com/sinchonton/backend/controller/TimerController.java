package com.sinchonton.backend.controller;

import com.sinchonton.backend.global.security.AuthUser;
import com.sinchonton.backend.service.TimerService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/timer")
public class TimerController {

    private final TimerService timerService;

    public TimerController(TimerService timerService) {
        this.timerService = timerService;
    }

    @PostMapping("/start")
    public void start(@AuthenticationPrincipal AuthUser authUser) {
        timerService.startTimer(authUser.getUserId());
    }

    @PostMapping("/pause")
    public void pause(@AuthenticationPrincipal AuthUser authUser) {
        timerService.pauseTimer(authUser.getUserId());
    }

    @PostMapping("/resume")
    public void resume(@AuthenticationPrincipal AuthUser authUser) {
        timerService.resumeTimer(authUser.getUserId());
    }

    @PostMapping("/stop")
    public void stop(@AuthenticationPrincipal AuthUser authUser) {
        timerService.stopTimer(authUser.getUserId());
    }

    @GetMapping("/today")
    public long today(@AuthenticationPrincipal AuthUser authUser) {
        return timerService.getTodayTotalSeconds(authUser.getUserId());
    }
}