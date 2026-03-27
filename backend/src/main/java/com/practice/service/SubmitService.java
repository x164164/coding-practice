package com.practice.service;

import com.practice.dto.SubmitAnswerRequest;
import com.practice.dto.SubmitAnswerResponse;
import com.practice.entity.Question;
import com.practice.entity.Submission;
import com.practice.entity.User;
import com.practice.entity.WrongNote;
import com.practice.mapper.SubmissionMapper;
import com.practice.mapper.UserMapper;
import com.practice.mapper.QuestionMapper;
import com.practice.mapper.WrongNoteMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubmitService {

    private final SubmissionMapper submissionMapper;
    private final UserMapper userMapper;
    private final QuestionMapper questionMapper;
    private final WrongNoteMapper wrongNoteMapper;

    public SubmitService(SubmissionMapper submissionMapper, 
                         UserMapper userMapper,
                         QuestionMapper questionMapper,
                         WrongNoteMapper wrongNoteMapper) {
        this.submissionMapper = submissionMapper;
        this.userMapper = userMapper;
        this.questionMapper = questionMapper;
        this.wrongNoteMapper = wrongNoteMapper;
    }

    /**
     * 提交答案
     */
    @Transactional
    public SubmitAnswerResponse submitAnswer(Long userId, SubmitAnswerRequest request) {
        // 获取题目
        Question question = questionMapper.selectById(request.getQuestionId());
        if (question == null) {
            throw new RuntimeException("题目不存在");
        }
        
        // 判断答案是否正确
        String userAnswer = request.getUserAnswer() != null ? request.getUserAnswer().trim() : "";
        String correctAnswer = question.getAnswer() != null ? question.getAnswer().trim() : "";
        boolean isCorrect = userAnswer.equalsIgnoreCase(correctAnswer);
        
        // 记录答题
        Submission submission = new Submission();
        submission.setUserId(userId);
        submission.setQuestionId(request.getQuestionId());
        submission.setUserAnswer(request.getUserAnswer());
        submission.setIsCorrect(isCorrect ? 1 : 0);
        submission.setScore(isCorrect ? question.getScore() : 0);
        submission.setTimeSpent(request.getTimeSpent());
        submission.setSubmitTime(LocalDateTime.now());
        submissionMapper.insert(submission);
        
        // 更新用户做题统计
        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setTotalQuestions(user.getTotalQuestions() + 1);
            if (isCorrect) {
                user.setCorrectCount(user.getCorrectCount() + 1);
            }
            userMapper.updateById(user);
        }
        
        // 更新题目统计
        question.setSubmitCount(question.getSubmitCount() + 1);
        // 简单计算正确率
        if (question.getSubmitCount() > 0) {
            // 需要重新查询正确数，这里简化处理
        }
        questionMapper.updateById(question);
        
        // 如果错误，加入错题本
        if (!isCorrect) {
            // 检查是否已存在
            List<WrongNote> exists = wrongNoteMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<WrongNote>()
                    .eq("user_id", userId)
                    .eq("question_id", request.getQuestionId())
            );
            
            if (exists.isEmpty()) {
                WrongNote wrongNote = new WrongNote();
                wrongNote.setUserId(userId);
                wrongNote.setQuestionId(request.getQuestionId());
                wrongNote.setUserAnswer(request.getUserAnswer());
                wrongNote.setCorrectAnswer(question.getAnswer());
                wrongNote.setWrongCount(1);
                wrongNote.setLastWrongTime(LocalDateTime.now());
                wrongNote.setIsReviewed(0);
                wrongNoteMapper.insert(wrongNote);
            } else {
                // 已存在，更新错误次数
                WrongNote note = exists.get(0);
                note.setWrongCount(note.getWrongCount() + 1);
                note.setUserAnswer(request.getUserAnswer());
                note.setLastWrongTime(LocalDateTime.now());
                wrongNoteMapper.updateById(note);
            }
        }
        
        // 返回结果
        SubmitAnswerResponse response = new SubmitAnswerResponse();
        response.setIsCorrect(isCorrect);
        response.setCorrectAnswer(question.getAnswer());
        response.setAnalysis(question.getAnalysis());
        response.setScore(isCorrect ? question.getScore() : 0);
        
        return response;
    }

    /**
     * 获取答题历史
     */
    public List<Submission> getSubmissionHistory(Long userId, Integer page, Integer size) {
        List<Submission> list = submissionMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Submission>()
                .eq("user_id", userId)
                .orderByDesc("submit_time")
                .last("LIMIT " + (page - 1) * size + "," + size)
        );
        return list;
    }

    /**
     * 获取错题本
     */
    public List<WrongNote> getWrongNotes(Long userId) {
        List<WrongNote> list = wrongNoteMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<WrongNote>()
                .eq("user_id", userId)
                .orderByDesc("last_wrong_time")
        );
        return list;
    }

    /**
     * 从错题本移除
     */
    public void removeFromWrongNotes(Long userId, Long questionId) {
        wrongNoteMapper.delete(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<WrongNote>()
                .eq("user_id", userId)
                .eq("question_id", questionId)
        );
    }
}