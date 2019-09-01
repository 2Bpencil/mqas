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
    List<Map<String,String>> knowledgeFrequency(Integer studentId,String knowledgeCode);


    /**
     *删除学生所有错题
     */
    void deleteAllByStudentId(Integer studentId);

    /**
     * 获取重点知识
     * @param studentId
     * @return
     */
    @Query(value = "SELECT COUNT(*) value ,knowledge_name name FROM wrong_question wq LEFT JOIN knowledge kn ON kn.`code` = wq.knowledge_code WHERE kn.type = 1 AND wq.student_id = ?1 GROUP BY wq.knowledge_name ", nativeQuery = true)
    List<Map<String,String>> getKeyKnowledgeWrongNum(Integer studentId);

    /**
     * 获取非重点知识
     * @param studentId
     * @return
     */
    @Query(value = "SELECT COUNT(*)  FROM wrong_question wq LEFT JOIN knowledge kn ON kn.`code` = wq.knowledge_code WHERE kn.type = 0 AND wq.student_id = ?1  ", nativeQuery = true)
    Integer getUnKeyKnowledgeWrongNum(Integer studentId);

}
