package com.tyf.mqas.code.dao;

import com.tyf.mqas.base.repository.ExpandJpaRepository;
import com.tyf.mqas.code.entity.Knowledge;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public interface KnowledgeRepository extends ExpandJpaRepository<Knowledge,Integer>{

    @Query(value = "select count(*) from knowledge where name = ?2 and id != ?1", nativeQuery = true)
    Integer getKnowledgeNumByIdAndName(Integer id, String name);

    @Query(value = "select count(*) from knowledge where name = ?1", nativeQuery = true)
    Integer getKnowledgeNumByName(String name);
    @Query(value = "select * from knowledge ORDER BY sort ASC", nativeQuery = true)
    List<Knowledge> findAllBySort();

    @Query(value = "select k.* from knowledge k left join r_classes_knowlwdge rck on k.id = rck.knowledge_id where rck.classes_id = ?1", nativeQuery = true)
    List<Knowledge> findKnowledgeByClassesId(Integer classesId);

    @Query(value = "select count(*) from r_classes_knowlwdge where knowledge_id = ?1", nativeQuery = true)
    Integer getRsNumByKnowledgeId(Integer knowledgeId);

    Knowledge findByCode(String code);

}