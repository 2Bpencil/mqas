package com.tyf.mqas.code.dao;

import com.tyf.mqas.code.entity.User;
import com.tyf.mqas.code.entity.WrongQuestion;
import com.tyf.mqas.utils.SecurityUtil;
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
    @Autowired
    private UserRepository userRepository;

    public List<WrongQuestion> getWrongQuestionByStudentIdAndConditions(Integer studentId,String code,String level,String startTime,String endTime){
        String sql = "select * from wrong_question where student_id = "+ studentId +" and time BETWEEN '"+startTime+"' AND '"+endTime+"' ";
        if(StringUtils.isNotBlank(code)){
            sql += " and knowledge_code in ("+code+") ";
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

    public List<WrongQuestion> getWrongQuestionByClassIdAndConditions(Integer classId,Integer type,String code,String level,String startTime,String endTime){
        List<WrongQuestion> wrongQuestionList = null;
        String sql = "";

        Object[] args = null;
        if(type == 0){
            //班级
            sql = "SELECT wq.* FROM wrong_question wq LEFT JOIN student stu ON wq.student_id = stu.id WHERE stu.classes_id = ?  and wq.time BETWEEN '"+startTime+"' AND '"+endTime+"' ";
            if(StringUtils.isNotBlank(code)){
                sql += " and wq.knowledge_code in ("+code+") ";
            }
            if(StringUtils.isNotBlank(level)){
                sql += " and wq.level = "+level+" ";
            }
            args = new Object[]{classId} ;
            wrongQuestionList = jdbcTemplate.query(sql,args,new BeanPropertyRowMapper<WrongQuestion>(WrongQuestion.class));
        }else{
            String userName = SecurityUtil.getCurUserName();
            User user = userRepository.findUserByUsername(userName);
            args = new Object[]{classId,user.getId()} ;
            //年级
            sql = "SELECT wq.* FROM wrong_question wq LEFT JOIN student stu ON wq.student_id = stu.id WHERE stu.classes_id IN ( SELECT cla.id FROM classes cla LEFT JOIN r_classes_user rcu ON cla.id = rcu.classes_id WHERE cla.pid = ? AND rcu.user_id = ?   ) and wq.time BETWEEN '"+startTime+"' AND '"+endTime+"' " ;
            if(StringUtils.isNotBlank(code)){
                sql += " and wq.knowledge_code = '"+code+"' ";
            }
            if(StringUtils.isNotBlank(level)){
                sql += " and wq.level = "+level+" ";
            }
            wrongQuestionList = jdbcTemplate.query(sql,args,new BeanPropertyRowMapper<WrongQuestion>(WrongQuestion.class));
        }
        return wrongQuestionList;
    }




}
    