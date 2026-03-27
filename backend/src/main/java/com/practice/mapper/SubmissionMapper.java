package com.practice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.practice.entity.Submission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SubmissionMapper extends BaseMapper<Submission> {
    
    @Select("SELECT COUNT(DISTINCT question_id) FROM submission WHERE user_id = #{userId}")
    Integer countDistinctQuestionsByUserId(Long userId);
    
    @Select("SELECT COUNT(*) FROM submission WHERE user_id = #{userId} AND is_correct = 1")
    Integer countCorrectByUserId(Long userId);
}