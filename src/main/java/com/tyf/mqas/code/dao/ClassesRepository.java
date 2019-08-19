package com.tyf.mqas.code.dao;

import com.tyf.mqas.base.repository.ExpandJpaRepository;
import com.tyf.mqas.code.entity.Classes;
import com.tyf.mqas.code.entity.Knowledge;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

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
    @Query(value = "select count(*) from r_classes_student where classes_id = ?1", nativeQuery = true)
    Integer getStudentRsNumByClassesId(Integer classesId);

    @Query(value = "select c.* from classes c left join r_classes_user rcu on c.id = rcu.classes_id where user_id = ?1 ORDER BY sort ASC ", nativeQuery = true)
    List<Classes> getClassesByUserId(Integer userId);


}