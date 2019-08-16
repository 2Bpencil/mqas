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

}