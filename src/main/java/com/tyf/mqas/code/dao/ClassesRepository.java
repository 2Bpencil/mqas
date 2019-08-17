package com.tyf.mqas.code.dao;

import com.tyf.mqas.base.repository.ExpandJpaRepository;
import com.tyf.mqas.code.entity.Classes;
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


}