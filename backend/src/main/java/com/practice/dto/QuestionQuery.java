package com.practice.dto;

import lombok.Data;

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