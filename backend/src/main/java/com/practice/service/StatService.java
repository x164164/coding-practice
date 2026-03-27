package com.practice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.practice.dto.ProgressResponse;
import com.practice.dto.RankingItem;
import com.practice.entity.User;
import com.practice.mapper.UserMapper;
import com.practice.mapper.SubmissionMapper;
import com.practice.mapper.QuestionMapper;
import com.practice.mapper.WrongNoteMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatService {

    private final UserMapper userMapper;
    private final SubmissionMapper submissionMapper;
    private final QuestionMapper questionMapper;
    private final WrongNoteMapper wrongNoteMapper;

    public StatService(UserMapper userMapper,
                       SubmissionMapper submissionMapper,
                       QuestionMapper questionMapper,
                       WrongNoteMapper wrongNoteMapper) {
        this.userMapper = userMapper;
        this.submissionMapper = submissionMapper;
        this.questionMapper = questionMapper;
        this.wrongNoteMapper = wrongNoteMapper;
    }

    /**
     * 获取用户进度
     */
    public ProgressResponse getProgress(Long userId) {
        // 获取用户信息
        User user = userMapper.selectById(userId);
        
        // 获取总题数
        Long totalQuestions = questionMapper.selectCount(null);
        
        // 获取用户已做题数（去重）
        Integer doneCount = submissionMapper.countDistinctQuestionsByUserId(userId);
        
        // 获取错题数
        Long wrongCount = wrongNoteMapper.selectCount(
            new QueryWrapper<WrongNote>().eq("user_id", userId)
        );
        
        // 计算进度
        int doneRate = totalQuestions > 0 ? (int) ((doneCount * 100.0) / totalQuestions) : 0;
        int correctRate = user.getTotalQuestions() > 0 
            ? (int) ((user.getCorrectCount() * 100.0) / user.getTotalQuestions()) 
            : 0;
        
        ProgressResponse response = new ProgressResponse();
        response.setTotalQuestions(totalQuestions.intValue());
        response.setDoneCount(doneCount);
        response.setDoneRate(doneRate);
        response.setCorrectCount(user.getCorrectCount());
        response.setWrongCount(wrongCount.intValue());
        response.setCorrectRate(correctRate);
        
        return response;
    }

    /**
     * 获取排行榜
     */
    public List<RankingItem> getRanking(String type, Integer limit) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        
        if ("total".equals(type)) {
            wrapper.orderByDesc("total_questions");
        } else {
            wrapper.orderByDesc("correct_count");
        }
        wrapper.last("LIMIT " + limit);
        
        List<User> users = userMapper.selectList(wrapper);
        
        List<RankingItem> ranking = new ArrayList<>();
        int rank = 1;
        for (User user : users) {
            RankingItem item = new RankingItem();
            item.setRank(rank++);
            item.setUserId(user.getId());
            item.setNickname(user.getNickname());
            item.setAvatar(user.getAvatar());
            item.setTotalQuestions(user.getTotalQuestions());
            item.setCorrectCount(user.getCorrectCount());
            
            int correctRate = user.getTotalQuestions() > 0 
                ? (int) ((user.getCorrectCount() * 100.0) / user.getTotalQuestions()) 
                : 0;
            item.setCorrectRate(correctRate);
            
            ranking.add(item);
        }
        
        return ranking;
    }

    /**
     * 获取系统概览（管理员）
     */
    public Object getOverview() {
        Long totalUsers = userMapper.selectCount(null);
        Long totalQuestions = questionMapper.selectCount(null);
        Long totalSubmissions = submissionMapper.selectCount(null);
        
        // 今日新增
        // ... 省略具体实现
        
        return java.util.Map.of(
            "totalUsers", totalUsers,
            "totalQuestions", totalQuestions,
            "totalSubmissions", totalSubmissions
        );
    }
}