package com.practice.controller;

import com.practice.dto.*;
import com.practice.service.StatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stat")
@Tag(name = "数据统计")
public class StatController {

    private final StatService statService;

    public StatController(StatService statService) {
        this.statService = statService;
    }

    @GetMapping("/progress")
    @Operation(summary = "获取用户进度")
    public ApiResponse<ProgressResponse> getProgress(@RequestAttribute("userId") Long userId) {
        ProgressResponse progress = statService.getProgress(userId);
        return ApiResponse.success(progress);
    }

    @GetMapping("/ranking")
    @Operation(summary = "获取排行榜")
    public ApiResponse<List<RankingItem>> getRanking(
            @RequestParam(defaultValue = "correct") String type,
            @RequestParam(defaultValue = "50") Integer limit) {
        List<RankingItem> ranking = statService.getRanking(type, limit);
        return ApiResponse.success(ranking);
    }

    @GetMapping("/overview")
    @Operation(summary = "获取系统概览")
    public ApiResponse<Object> getOverview() {
        Object overview = statService.getOverview();
        return ApiResponse.success(overview);
    }
}