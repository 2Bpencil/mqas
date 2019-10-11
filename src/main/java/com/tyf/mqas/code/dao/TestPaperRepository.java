package com.tyf.mqas.code.dao;

import com.tyf.mqas.base.repository.ExpandJpaRepository;
import com.tyf.mqas.code.entity.TestPaper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TestPaperRepository extends ExpandJpaRepository<TestPaper,Integer> {



    @Query(value = "select count(*) from test_paper where name = ?1", nativeQuery = true)
    Integer getTestPaperByName(String name);
}
