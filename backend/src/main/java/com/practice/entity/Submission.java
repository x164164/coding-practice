package com.practice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("submission")
public class Submission {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;            // 用户ID
    private Long questionId;       // 题目ID
    private String userAnswer;      // 用户答案
    private Integer isCorrect;     // 是否正确: 0错误 1正确
    private Integer score;          // 得分
    private Integer timeSpent;      // 答题耗时(秒)
    private String ipAddress;       // IP地址
    private String device;          // 设备信息
    private LocalDateTime submitTime; // 提交时间
    
    @TableField(exist = false)
    private Question question;      // 题目信息
    
    @TableField(exist = false)
    private User user;              // 用户信息
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}