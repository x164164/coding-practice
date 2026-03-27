package com.practice.dto;

import lombok.Data;

@Data
public class SubmitAnswerRequest {
    private Long questionId;
    private String userAnswer;
    private Integer timeSpent;
}