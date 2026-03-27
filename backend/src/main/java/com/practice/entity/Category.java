package com.practice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("category")
public class Category {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;           // 分类名称
    private String icon;          // 图标
    private String description;    // 描述
    private Integer sort;         // 排序
    private Integer questionCount; // 题目数量
    private Integer status;       // 状态: 0禁用 1启用
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}