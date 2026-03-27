package com.practice.dto;

import lombok.Data;

@Data
public class SubmitAnswerResponse {
    private Boolean isCorrect;
    private String correctAnswer;
    private String analysis;
    private Integer score;
}