package com.tyf.mqas.code.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @ClassName WrongQuestion
 * @Description: TODO
 * @Author tyf
 * @Date 2019/8/20 
 * @Version V1.0
 **/
@Entity
@Table(name = "wrong_question")
public class WrongQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 100)
    private String name;
    @Column(length = 100)
    //知识编码
    private String knowledgeCode;
    @Column
    private String time;
    @Column
    private Integer studentId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKnowledgeCode() {
        return knowledgeCode;
    }

    public void setKnowledgeCode(String knowledgeCode) {
        this.knowledgeCode = knowledgeCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
}
    