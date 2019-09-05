package com.tyf.mqas.code.dao;

import com.tyf.mqas.base.repository.ExpandJpaRepository;
import com.tyf.mqas.code.entity.Classes;
import com.tyf.mqas.code.entity.Knowledge;
import com.tyf.mqas.code.entity.WrongQuestion;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public interface ClassesRepository extends ExpandJpaRepository<Classes,Integer>{

    @Query(value = "select count(*) from classes where name = ?2 and id != ?1 and pid = ?3", nativeQuery = true)
    Integer getClassesNumByIdAndName(Integer id, String name,Integer pid);

    @Query(value = "select count(*) from classes where name = ?1 and pid = ?2", nativeQuery = true)
    Integer getClassesNumByName(String name,Integer pid);

    @Query(value = "select * from classes ORDER BY sort ASC", nativeQuery = true)
    List<Classes> findAllBySort();

    @Query(value = "INSERT INTO r_classes_knowlwdge(classes_id,knowledge_id) VALUES(?1,?2)", nativeQuery = true)
    @Modifying
    @Transactional
    void saveKnowledgeSet(Integer calssesId,Integer knowledgeId);

    @Query(value = "delete from r_classes_knowlwdge where classes_id = ?1", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteKnowledgeSet(Integer calssesId);

    @Query(value = "select count(*) from r_classes_user where classes_id = ?1", nativeQuery = true)
    Integer getUserRsNumByClassesId(Integer classesId);
    @Query(value = "select count(*) from student where classes_id = ?1", nativeQuery = true)
    Integer getStudentNumByClassesId(Integer classesId);

    @Query(value = "select c.* from classes c left join r_classes_user rcu on c.id = rcu.classes_id where user_id = ?1 ORDER BY sort ASC ", nativeQuery = true)
    List<Classes> getClassesByUserId(Integer userId);

    /**
     * 获取所有年级
     * @return
     */
    @Query(value = "select * from classes where pid = 0 ORDER BY sort ASC", nativeQuery = true)
    List<Classes> getAllGrade();

    /**
     * 获取年级下的班级
     * @return
     */
    List<Classes> findAllByPid(Integer pid);

    /**
     * 根据班级id获取全班所有学生错题的知识点
     * @return
     */
    @Query(value = "select DISTINCT wq.knowledge_name name,wq.knowledge_code code from wrong_question wq WHERE wq.student_id IN (SELECT stu.id FROM student stu WHERE stu.classes_id = ?1 ) AND wq.time BETWEEN ?2 AND ?3 LIMIT 0,5 ", nativeQuery = true)
    List<Map<String,String>> findWrongKnowledgeByClassId(Integer id,String startDate,String endDate);

    @Query(value = "SELECT COUNT(*) FROM student stu WHERE stu.id IN (SELECT wq.student_id FROM wrong_question wq WHERE wq.knowledge_code = ?2 AND wq.time BETWEEN ?3 AND ?4) AND stu.classes_id = ?1", nativeQuery = true)
    Integer findStudentNumByClassIdAnAndCode(Integer id,String code,String startDate,String endDate);

    @Query(value = "select COUNT(*) from student where classes_id = ?1 ", nativeQuery = true)
    Integer findAllStudentNumByClassId(Integer id);

}