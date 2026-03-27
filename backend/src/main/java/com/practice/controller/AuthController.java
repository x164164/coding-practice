package com.practice.controller;

import com.practice.dto.*;
import com.practice.entity.User;
import com.practice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证管理")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "微信登录")
    public ApiResponse<LoginResponse> wechatLogin(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.wechatLogin(request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/login/username")
    @Operation(summary = "用户名登录")
    public ApiResponse<LoginResponse> usernameLogin(@RequestBody Map<String, String> params) {
        try {
            LoginResponse response = authService.usernameLogin(
                params.get("username"), 
                params.get("password")
            );
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public ApiResponse<LoginResponse> register(@Validated @RequestBody RegisterRequest request) {
        try {
            LoginResponse response = authService.register(request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public ApiResponse<User> getUserInfo(@RequestAttribute("userId") Long userId) {
        User user = authService.getUserInfo(userId);
        return ApiResponse.success(user);
    }

    @PutMapping("/info")
    @Operation(summary = "更新用户信息")
    public ApiResponse<Void> updateUserInfo(
            @RequestAttribute("userId") Long userId,
            @RequestBody UserUpdateRequest request) {
        authService.updateUserInfo(userId, request);
        return ApiResponse.success();
    }
}