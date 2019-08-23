package com.tyf.mqas.code.dao;

import com.tyf.mqas.base.repository.ExpandJpaRepository;
import com.tyf.mqas.code.entity.WrongQuestion;
import org.springframework.stereotype.Repository;

@Repository
public interface WrongQuestionRepository extends ExpandJpaRepository<WrongQuestion,Integer> {



}
