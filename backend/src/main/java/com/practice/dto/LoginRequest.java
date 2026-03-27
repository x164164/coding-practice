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