package com.practice.dto;

import lombok.Data;

@Data
public class ProgressResponse {
    private Integer totalQuestions;     // 总题数
    private Integer doneCount;         // 已做题数
    private Integer doneRate;          // 完成进度%
    private Integer correctCount;      // 正确数
    private Integer wrongCount;        // 错题数
    private Integer correctRate;       // 正确率%
}