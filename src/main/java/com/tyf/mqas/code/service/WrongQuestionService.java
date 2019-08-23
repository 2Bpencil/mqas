package com.tyf.mqas.code.service;

import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.base.datapage.PageGetter;
import com.tyf.mqas.code.dao.WrongQuestionRepository;
import com.tyf.mqas.code.entity.WrongQuestion;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
@Transactional
public class WrongQuestionService extends PageGetter<WrongQuestion>{

    @Autowired
    private WrongQuestionRepository wrongQuestionRepository;

    /**
     * 分页查询
     * @param parameterMap
     * @return
     */
    public DataPage<WrongQuestion> getDataPage(Map<String,String[]> parameterMap,Integer id,String startTime,String endTime){
        String sql = "SELECT * FROM wrong_question  WHERE wrong_question.student_id = "+id;
        String countSql = "SELECT COUNT(*) FROM wrong_question  WHERE wrong_question.student_id = "+id;
        //开始结束时间都不为空
        if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
            sql = "SELECT * FROM wrong_question  WHERE wrong_question.student_id = "+id +" AND time BETWEEN '"+startTime+"' AND '" +endTime+"'";
            countSql = "SELECT COUNT(*) FROM wrong_question  WHERE wrong_question.student_id = "+id +" AND time BETWEEN '"+startTime+"' AND '" +endTime+"'";
        }else if(StringUtils.isNotBlank(startTime) && StringUtils.isBlank(endTime)){
            //开始时间不为空
            sql = "SELECT * FROM wrong_question  WHERE wrong_question.student_id = "+id +" AND time > '"+startTime+"'";
            countSql = "SELECT COUNT(*) FROM wrong_question  WHERE wrong_question.student_id = "+id +" AND time > '"+startTime+"'";
        }else if(StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)){
            //结束时间不为空
            sql = "SELECT * FROM wrong_question  WHERE wrong_question.student_id = "+id +" AND time  < '" +endTime+"'";
            countSql = "SELECT COUNT(*) FROM wrong_question  WHERE wrong_question.student_id = "+id +" AND time < '" +endTime+"'";
        }
        return super.getPages(parameterMap,sql,countSql);
    }



    /**
     * 删除实体
     * @param id
     */
    public void deleteWrongQuestion(Integer id){
        wrongQuestionRepository.deleteById(id);
    }




}