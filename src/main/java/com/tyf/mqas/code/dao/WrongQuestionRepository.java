package com.tyf.mqas.code.dao;

import com.tyf.mqas.base.repository.ExpandJpaRepository;
import com.tyf.mqas.code.entity.WrongQuestion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface WrongQuestionRepository extends ExpandJpaRepository<WrongQuestion,Integer> {

    @Query(value = "SELECT DISTINCT knowledge_name,knowledge_code FROM wrong_question WHERE student_id = ?1 AND time BETWEEN ?2 AND ?3", nativeQuery = true)
    List<Map<String,Object>> getAllKnowledgeInfoByStudentIdAndTime(Integer studentId,String startDate,String endDate);


    @Query(value = "SELECT COUNT(*) FROM wrong_question WHERE student_id = ?1 AND knowledge_code = ?2 AND time BETWEEN ?3 AND ?4", nativeQuery = true)
    Integer getRongNumByStudentIdAndKnowledgeCode(Integer studentId,String knowledgeCode,String startDate,String endDate);

    @Query(value = "SELECT DISTINCT knowledge_name,knowledge_code FROM wrong_question WHERE student_id = ?1", nativeQuery = true)
    List<Map<String,Object>> getAllKnowledgeInfoByStudentId(Integer studentId);


    @Query(value = "SELECT COUNT(*) num,time FROM wrong_question WHERE student_id = ?1 AND knowledge_code = ?2 GROUP BY time ORDER BY time ASC", nativeQuery = true)
    List<Map<String,String>>  knowledgeFrequency(Integer studentId,String knowledgeCode);

    void deleteAllByStudentId(Integer StudentId);
}
