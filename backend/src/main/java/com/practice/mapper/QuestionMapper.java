package com.practice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.practice.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
    
    IPage<Question> selectPageByCondition(Page<Question> page, 
            @Param("category") String category,
            @Param("difficulty") Integer difficulty,
            @Param("type") String type,
            @Param("keyword") String keyword,
            @Param("status") String status);
}