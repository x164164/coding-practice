package com.practice.service;

import com.practice.dto.*;
import com.practice.entity.User;
import com.practice.mapper.UserMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserMapper userMapper;
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public AuthService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 微信登录
     */
    public LoginResponse wechatLogin(LoginRequest request) {
        // TODO: 实际需要调用微信API获取openid
        // 这里简化处理，直接用code作为标识
        String openid = "wx_" + request.getCode();
        
        User user = userMapper.findByOpenid(openid);
        boolean isNew = false;
        
        if (user == null) {
            // 创建新用户
            user = new User();
            user.setOpenid(openid);
            user.setNickname(request.getNickname() != null ? request.getNickname() : "新用户");
            user.setAvatar(request.getAvatar());
            user.setRole("student");
            user.setStatus(1);
            userMapper.insert(user);
            isNew = true;
        }
        
        // 检查是否被禁用
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }
        
        // 生成Token
        String token = generateToken(user.getId(), user.getRole());
        
        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setNickname(user.getNickname());
        response.setAvatar(user.getAvatar());
        response.setRole(user.getRole());
        response.setToken(token);
        response.setIsNew(isNew);
        
        return response;
    }

    /**
     * 用户名密码登录
     */
    public LoginResponse usernameLogin(String username, String password) {
        User user = userMapper.findByUsername(username);
        
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }
        
        // TODO: 密码验证 BCrypt
        // if (!BCrypt.checkpw(password, user.getPassword())) {
        //     throw new RuntimeException("密码错误");
        // }
        
        String token = generateToken(user.getId(), user.getRole());
        
        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setNickname(user.getNickname());
        response.setAvatar(user.getAvatar());
        response.setRole(user.getRole());
        response.setToken(token);
        
        return response;
    }

    /**
     * 注册
     */
    public LoginResponse register(RegisterRequest request) {
        // 检查用户名是否存在
        User existUser = userMapper.findByUsername(request.getUsername());
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        // TODO: 密码加密
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setRole("student");
        user.setStatus(1);
        
        userMapper.insert(user);
        
        String token = generateToken(user.getId(), user.getRole());
        
        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setNickname(user.getNickname());
        response.setAvatar(user.getAvatar());
        response.setRole(user.getRole());
        response.setToken(token);
        response.setIsNew(true);
        
        return response;
    }

    /**
     * 获取用户信息
     */
    public User getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setPassword(null); // 不返回密码
        }
        return user;
    }

    /**
     * 更新用户信息
     */
    public void updateUserInfo(Long userId, UserUpdateRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (StrUtil.isNotBlank(request.getNickname())) {
            user.setNickname(request.getNickname());
        }
        if (StrUtil.isNotBlank(request.getAvatar())) {
            user.setAvatar(request.getAvatar());
        }
        if (StrUtil.isNotBlank(request.getPhone())) {
            user.setPhone(request.getPhone());
        }
        if (StrUtil.isNotBlank(request.getEmail())) {
            user.setEmail(request.getEmail());
        }
        
        userMapper.updateById(user);
    }

    /**
     * 生成JWT Token
     */
    private String generateToken(Long userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        
        return Jwts.builder()
                .setClaims(claims)
                .setId(IdUtil.fastSimpleUUID())
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}