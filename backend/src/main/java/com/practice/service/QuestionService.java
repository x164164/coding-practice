package com.practice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.dto.PageResponse;
import com.practice.dto.QuestionQuery;
import com.practice.dto.QuestionRequest;
import com.practice.entity.Question;
import com.practice.mapper.QuestionMapper;
import org.springframework.stereotype.Service;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    private final QuestionMapper questionMapper;
    private final ObjectMapper objectMapper;

    public QuestionService(QuestionMapper questionMapper, ObjectMapper objectMapper) {
        this.questionMapper = questionMapper;
        this.objectMapper = objectMapper;
    }

    /**
     * 分页查询题目
     */
    public PageResponse<Question> getQuestionList(QuestionQuery query) {
        Page<Question> page = new Page<>(query.getPage(), query.getSize());
        
        IPage<Question> result = questionMapper.selectPageByCondition(page,
                query.getCategory(),
                query.getDifficulty(),
                query.getType(),
                query.getKeyword(),
                query.getStatus());
        
        PageResponse<Question> response = new PageResponse<>();
        response.setTotal(Long.valueOf(result.getTotal()));
        response.setPage(Integer.valueOf(result.getCurrent().intValue()));
        response.setSize(Integer.valueOf(result.getSize().intValue()));
        response.setRecords(result.getRecords());
        
        return response;
    }

    /**
     * 获取题目详情
     */
    public Question getQuestionById(Long id) {
        Question question = questionMapper.selectById(id);
        if (question != null) {
            // 增加查看次数
            question.setViewCount(question.getViewCount() + 1);
            questionMapper.updateById(question);
        }
        return question;
    }

    /**
     * 添加题目
     */
    public Long addQuestion(QuestionRequest request, Long userId) {
        Question question = new Question();
        question.setTitle(request.getTitle());
        question.setContent(request.getContent());
        question.setType(request.getType() != null ? request.getType() : "choice");
        question.setAnswer(request.getAnswer());
        question.setAnalysis(request.getAnalysis());
        question.setDifficulty(request.getDifficulty() != null ? request.getDifficulty() : 1);
        question.setCategory(request.getCategory());
        question.setTags(request.getTags());
        question.setImageUrl(request.getImageUrl());
        question.setScore(request.getScore() != null ? request.getScore() : 10);
        question.setCreatedBy(userId);
        question.setStatus(1);
        
        // 处理选项
        if (request.getOptions() != null && !request.getOptions().isEmpty()) {
            try {
                question.setOptions(objectMapper.writeValueAsString(request.getOptions()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("选项序列化失败");
            }
        }
        
        questionMapper.insert(question);
        return question.getId();
    }

    /**
     * 更新题目
     */
    public void updateQuestion(Long id, QuestionRequest request) {
        Question question = questionMapper.selectById(id);
        if (question == null) {
            throw new RuntimeException("题目不存在");
        }
        
        if (StrUtil.isNotBlank(request.getTitle())) {
            question.setTitle(request.getTitle());
        }
        if (StrUtil.isNotBlank(request.getContent())) {
            question.setContent(request.getContent());
        }
        if (request.getType() != null) {
            question.setType(request.getType());
        }
        if (request.getAnswer() != null) {
            question.setAnswer(request.getAnswer());
        }
        if (request.getAnalysis() != null) {
            question.setAnalysis(request.getAnalysis());
        }
        if (request.getDifficulty() != null) {
            question.setDifficulty(request.getDifficulty());
        }
        if (request.getCategory() != null) {
            question.setCategory(request.getCategory());
        }
        if (request.getTags() != null) {
            question.setTags(request.getTags());
        }
        
        if (request.getOptions() != null) {
            try {
                question.setOptions(objectMapper.writeValueAsString(request.getOptions()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("选项序列化失败");
            }
        }
        
        questionMapper.updateById(question);
    }

    /**
     * 删除题目
     */
    public void deleteQuestion(Long id) {
        questionMapper.deleteById(id);
    }

    /**
     * 获取所有分类
     */
    public List<String> getCategories() {
        // 这里简化处理，实际可以从category表查询
        List<String> categories = new ArrayList<>();
        categories.add("数组");
        categories.add("字符串");
        categories.add("链表");
        categories.add("树");
        categories.add("动态规划");
        categories.add("贪心算法");
        categories.add("回溯算法");
        categories.add("分治算法");
        return categories;
    }
}