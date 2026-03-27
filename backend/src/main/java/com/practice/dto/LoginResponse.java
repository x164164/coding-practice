package com.practice.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private Long userId;
    private String nickname;
    private String avatar;
    private String role;
    private String token;
    private Boolean isNew;          // 是否新用户
}