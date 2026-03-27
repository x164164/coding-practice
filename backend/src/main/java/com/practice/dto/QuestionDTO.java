package com.practice.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class QuestionRequest {
    @NotBlank(message = "题目标题不能为空")
    private String title;
    
    @NotBlank(message = "题目内容不能为空")
    private String content;
    
    private String type = "choice";     // 类型
    private List<Option> options;       // 选择题选项
    private String answer;              // 正确答案
    private String analysis;            // 解析
    private Integer difficulty = 1;     // 难度
    private String category;            // 分类
    private String tags;                // 标签
    private String imageUrl;            // 图片
    private Integer score = 10;         // 分值
    
    @Data
    public static class Option {
        private String label;   // A, B, C, D
        private String content; // 选项内容
    }
}

@Data
public class QuestionQuery {
    private Integer page = 1;
    private Integer size = 20;
    private String category;
    private Integer difficulty;
    private String type;
    private String keyword;
    private String status;
}

@Data
public class SubmitAnswerRequest {
    private Long questionId;
    private String userAnswer;
    private Integer timeSpent;
}

@Data
public class SubmitAnswerResponse {
    private Boolean isCorrect;
    private String correctAnswer;
    private String analysis;
    private Integer score;
}