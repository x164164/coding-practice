package com.practice.dto;

import lombok.Data;

@Data
public class RankingItem {
    private Integer rank;
    private Long userId;
    private String nickname;
    private String avatar;
    private Integer totalQuestions;
    private Integer correctCount;
    private Integer correctRate;
}