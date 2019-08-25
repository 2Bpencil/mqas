package com.tyf.mqas.code.dao;

import com.tyf.mqas.base.repository.ExpandJpaRepository;
import com.tyf.mqas.code.entity.TestPaperType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestPaperTypeRepository extends ExpandJpaRepository<TestPaperType,Integer> {

    List<TestPaperType> findAllByTestPaperId(Integer testPaperId);

}
