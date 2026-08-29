package com.sinchonton.backend.controller;

import com.sinchonton.backend.service.TimerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/timer")
public class TimerController {

    private final TimerService timerService;

    public TimerController(TimerService timerService) {
        this.timerService = timerService;
    }

    @PostMapping("/start")
    public void start(@RequestParam Long userId) {
        // 나중에 userId는 JWT 토큰에서 꺼내는 방식으로 바뀔 예정 (A 작업 완료되면)
        timerService.startTimer(userId);
    }

    @PostMapping("/stop")
    public void stop(@RequestParam Long userId) {
        timerService.stopTimer(userId);
    }

    @GetMapping("/today")
    public long today(@RequestParam Long userId) {
        return timerService.getTodayTotalSeconds(userId);
    }
}