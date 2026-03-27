package com.practice.dto;

import lombok.Data;
import java.util.List;

@Data
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;
    
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }
    
    public static <T> ApiResponse<T> success() {
        return success(null);
    }
    
    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(500);
        response.setMessage(message);
        return response;
    }
    
    public static <T> ApiResponse<T> error(Integer code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}

@Data
public class PageResponse<T> {
    private Long total;
    private Integer page;
    private Integer size;
    private List<T> records;
}

@Data
public class ProgressResponse {
    private Integer totalQuestions;     // 总题数
    private Integer doneCount;         // 已做题数
    private Integer doneRate;          // 完成进度%
    private Integer correctCount;      // 正确数
    private Integer wrongCount;        // 错题数
    private Integer correctRate;       // 正确率%
}

@Data
public class RankingItem {
    private Integer rank;
    private Long userId;
    private String nickname;
    private String avatar;
    private Integer totalQuestions;
    private Integer correctCount;
    private Integer correctRate;
}