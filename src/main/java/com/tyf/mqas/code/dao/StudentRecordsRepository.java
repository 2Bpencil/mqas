package com.tyf.mqas.code.dao;

import com.tyf.mqas.base.repository.ExpandJpaRepository;
import com.tyf.mqas.code.entity.StudentRecords;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StudentRecordsRepository extends ExpandJpaRepository<StudentRecords,Integer> {

    @Query(value = "select count(*) from student_records where year = ?1 ", nativeQuery = true)
    Integer getAllNumByYear(Integer year);

    StudentRecords findByYearAndMonth(Integer year,Integer month);

    List<StudentRecords> findAllByYear(Integer year);
    @Query(value = "select distinct year from student_records  ", nativeQuery = true)
    List<Integer> getAllYear();

}
