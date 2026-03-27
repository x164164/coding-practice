package com.practice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String openid;          // 微信openid
    private String username;        // 用户名
    private String nickname;       // 昵称
    private String password;        // 密码
    private String phone;           // 手机号
    private String email;           // 邮箱
    private String avatar;          // 头像URL
    private String role;           // 角色: student/admin
    private Integer status;         // 状态: 0禁用 1正常
    
    @TableField(exist = false)
    private Integer totalQuestions;   // 总做题数 (不映射数据库)
    
    @TableField(exist = false)
    private Integer correctCount;      // 正确数 (不映射数据库)
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}