package com.practice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("wrong_note")
public class WrongNote {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;            // 用户ID
    private Long questionId;       // 题目ID
    private String userAnswer;      // 用户的错误答案
    private String correctAnswer;   // 正确答案
    private Integer wrongCount;     // 错误次数
    private LocalDateTime lastWrongTime; // 最后一次错误时间
    private Integer isReviewed;     // 是否已复习: 0否 1是
    
    @TableField(exist = false)
    private Question question;      // 题目信息
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}