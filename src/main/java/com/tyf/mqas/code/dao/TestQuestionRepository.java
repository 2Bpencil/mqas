package com.tyf.mqas.code.dao;

import com.tyf.mqas.base.repository.ExpandJpaRepository;
import com.tyf.mqas.code.entity.TestQuestion;
import org.springframework.stereotype.Repository;

@Repository
public interface TestQuestionRepository extends ExpandJpaRepository<TestQuestion,Integer> {

    void deleteByTestPaperTypeId(Integer testPaperTypeId);

}
