package com.tyf.mqas;

import com.tyf.mqas.code.dao.StudentRepository;
import com.tyf.mqas.code.entity.Student;
import com.tyf.mqas.code.service.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MqasApplicationTests {
    @Autowired
    private StudentService service;
    @Autowired
    private StudentRepository repository;

    @Test
    public void contextLoads() {


        Student student = repository.getStudentById(1);
        System.out.println(student.getName());

    }

}
