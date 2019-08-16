package com.tyf.mqas.code.entity;

import javax.persistence.*;

/**
/**
 * @ClassName Knowledge
 * @Description: 知识点
 * @Author tyf
 * @Date 2019/8/16 
 * @Version V1.0
 **/
@Entity
@Table(name = "knowledge")
public class Knowledge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer pid;
    @Column(length = 100)
    private String name;
    @Column(length = 50)
    /**
     * uuid
     */
    private String code;
    @Column(length = 500)
    private String path;
    @Column
    private Integer sort;


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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
    