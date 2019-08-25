package com.tyf.mqas.code.entity;

import javax.persistence.*;

@Entity
@Table(name = "test_question")
public class TestQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 225)
    private String name;
    @Column(length = 100)
    private String knowledgeCode;
    @Column(length = 100)
    private String knowledgeName;
    @Column
    private Integer testPaperTypeId;

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

    public Integer getTestPaperTypeId() {
        return testPaperTypeId;
    }

    public void setTestPaperTypeId(Integer testPaperTypeId) {
        this.testPaperTypeId = testPaperTypeId;
    }

    public String getKnowledgeName() {
        return knowledgeName;
    }

    public void setKnowledgeName(String knowledgeName) {
        this.knowledgeName = knowledgeName;
    }
}
