package com.tyf.mqas.code.entity;

import javax.persistence.*;
/**
* @Description:   试卷
* @Author: Mr.Tan
* @Date: 2019/10/10 14:39
*/
@Entity
@Table(name = "test_paper")
public class TestPaper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 100)
    private String name;
    @Column
    private String time;
    @Column
    //科目类型
    private Integer type;
    @Column(length = 100)
    private String fileName;
    @Column(length = 100)
    private String saveFileName;


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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }
}
