package com.practice.controller;

import com.practice.dto.*;
import com.practice.dto.QuestionDTO.*;
import com.practice.entity.Question;
import com.practice.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/question")
@Tag(name = "题目管理")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/list")
    @Operation(summary = "获取题目列表")
    public ApiResponse<PageResponse<Question>> getQuestionList(QuestionQuery query) {
        PageResponse<Question> result = questionService.getQuestionList(query);
        return ApiResponse.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取题目详情")
    public ApiResponse<Question> getQuestion(@PathVariable Long id) {
        Question question = questionService.getQuestionById(id);
        return ApiResponse.success(question);
    }

    @PostMapping
    @Operation(summary = "添加题目")
    public ApiResponse<Long> addQuestion(
            @RequestAttribute("userId") Long userId,
            @Valid @RequestBody QuestionRequest request) {
        // 检查是否为管理员
        // 这里简化，实际应该从上下文中获取角色判断
        Long id = questionService.addQuestion(request, userId);
        return ApiResponse.success(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新题目")
    public ApiResponse<Void> updateQuestion(
            @PathVariable Long id,
            @RequestBody QuestionRequest request) {
        questionService.updateQuestion(id, request);
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除题目")
    public ApiResponse<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ApiResponse.success();
    }

    @GetMapping("/categories")
    @Operation(summary = "获取所有分类")
    public ApiResponse<List<String>> getCategories() {
        List<String> categories = questionService.getCategories();
        return ApiResponse.success(categories);
    }
}