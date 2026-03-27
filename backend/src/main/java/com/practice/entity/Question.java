package com.practice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("question")
public class Question {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;           // 题目标题
    private String content;         // 题目内容
    private String type;            // 类型: choice/code/answer
    private String options;        // 选择题选项 (JSON)
    private String answer;          // 正确答案
    private String analysis;        // 答案解析
    private Integer difficulty;     // 难度: 1简单 2中等 3困难
    private String category;        // 分类
    private String tags;            // 标签
    private String imageUrl;        // 题目图片URL
    private Integer score;          // 分值
    private Long createdBy;         // 创建者ID
    
    @TableField(exist = false)
    private String createdByName;   // 创建者名称
    
    private Integer viewCount;      // 查看次数
    private Integer submitCount;    // 提交次数
    private Double correctRate;     // 正确率
    private Integer status;         // 状态: 0禁用 1启用
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}