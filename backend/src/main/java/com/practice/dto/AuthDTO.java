package com.practice.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank(message = "code不能为空")
    private String code;           // 微信登录code
    private String nickname;        // 昵称
    private String avatar;          // 头像
    private String phone;          // 手机号(可选)
}

@Data
public class LoginResponse {
    private Long userId;
    private String nickname;
    private String avatar;
    private String role;
    private String token;
    private Boolean isNew;          // 是否新用户
}

@Data
public class RegisterRequest {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    private String nickname;
    private String phone;
    private String email;
}

@Data
public class UserUpdateRequest {
    private String nickname;
    private String avatar;
    private String phone;
    private String email;
}