package com.practice.controller;

import com.practice.dto.*;
import com.practice.entity.Submission;
import com.practice.entity.WrongNote;
import com.practice.service.SubmitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "答题与统计")
public class SubmitController {

    private final SubmitService submitService;

    public SubmitController(SubmitService submitService) {
        this.submitService = submitService;
    }

    @PostMapping("/submit")
    @Operation(summary = "提交答案")
    public ApiResponse<SubmitAnswerResponse> submitAnswer(
            @RequestAttribute("userId") Long userId,
            @RequestBody SubmitAnswerRequest request) {
        try {
            SubmitAnswerResponse response = submitService.submitAnswer(userId, request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/submission/history")
    @Operation(summary = "获取答题历史")
    public ApiResponse<List<Submission>> getSubmissionHistory(
            @RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        List<Submission> list = submitService.getSubmissionHistory(userId, page, size);
        return ApiResponse.success(list);
    }

    @GetMapping("/wrong-notes")
    @Operation(summary = "获取错题本")
    public ApiResponse<List<WrongNote>> getWrongNotes(
            @RequestAttribute("userId") Long userId) {
        List<WrongNote> list = submitService.getWrongNotes(userId);
        return ApiResponse.success(list);
    }

    @DeleteMapping("/wrong-notes/{questionId}")
    @Operation(summary = "从错题本移除")
    public ApiResponse<Void> removeWrongNote(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long questionId) {
        submitService.removeFromWrongNotes(userId, questionId);
        return ApiResponse.success();
    }
}