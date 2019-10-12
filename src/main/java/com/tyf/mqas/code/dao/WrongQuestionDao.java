package com.tyf.mqas.code.dao;

import com.tyf.mqas.code.entity.WrongQuestion;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 
 * @Author: tyf
 * @Date: 2019/10/12 14:30
 */
@Repository
public class WrongQuestionDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<WrongQuestion> getWrongQuestionByStudentIdAndConditions(Integer studentId,String code,String level,String startTime,String endTime){
        String sql = "select * from wrong_question where student_id = "+ studentId +" and time BETWEEN '"+startTime+"' AND '"+endTime+"' ";
        if(StringUtils.isNotBlank(code)){
            sql += " and knowledge_code = '"+code+"' ";
        }
        if(StringUtils.isNotBlank(level)){
            sql += " and level = "+level+" ";
        }
        List<WrongQuestion> list;
        try {
            list = jdbcTemplate.query(sql,new BeanPropertyRowMapper<WrongQuestion>(WrongQuestion.class));
        } catch (Exception e) {
            list = new ArrayList<>();
            e.printStackTrace();
        }
        return list;
    }


}
    