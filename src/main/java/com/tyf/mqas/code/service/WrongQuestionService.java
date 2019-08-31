package com.tyf.mqas.code.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.base.datapage.PageGetter;
import com.tyf.mqas.code.dao.WrongQuestionRepository;
import com.tyf.mqas.code.entity.WrongQuestion;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    /**
     * 知识点错误次数数据
     * @param studentId
     * @return
     */
    public String getknowledgePointErrorNumJson(Integer studentId,String startDate,String endDate){
        Map<String,Object> dataMap = new HashMap<>();
        List<Map<String,Object>> knowledgeInfoList = wrongQuestionRepository.getAllKnowledgeInfoByStudentIdAndTime(studentId,startDate,endDate);
        List<String> xAxis = new ArrayList<>();
        List<Integer> wrongNumList = new ArrayList<>();
        knowledgeInfoList.forEach(map->{
            xAxis.add(map.get("knowledge_name").toString());
            wrongNumList.add(wrongQuestionRepository.getRongNumByStudentIdAndKnowledgeCode(studentId,map.get("knowledge_code").toString(),startDate,endDate));
        });
        dataMap.put("xAxis",xAxis);
        dataMap.put("data",wrongNumList);
        return JSONObject.toJSONString(dataMap);
    }

    /**
     * 获取该学生所有错误知识点
     * @return
     */
    public String getAllKnowledge(Integer studentId){
        List<Map<String,Object>> knowledgeInfoList = wrongQuestionRepository.getAllKnowledgeInfoByStudentId(studentId);
        return JSONArray.toJSONString(knowledgeInfoList);
    }


    /**
     * 知识点频次分析
     * @param studentId
     * @param code
     * @return
     */
    public String knowledgeFrequency(Integer studentId,String code){

        Map<String,Object> dataMap = new HashMap<>();
        List<String> xAxis = new ArrayList<>();
        List<String> dataList = new ArrayList<>();
        List<Map<String,String>> list = wrongQuestionRepository.knowledgeFrequency(studentId,code);
        list.forEach(map->{
            xAxis.add(map.get("time"));
            dataList.add(map.get("num"));
        });
        dataMap.put("xAxis",xAxis);
        dataMap.put("data",dataList);
        return JSONObject.toJSONString(dataMap);
    }


}