package com.practice.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String nickname;
    private String avatar;
    private String phone;
    private String email;
}