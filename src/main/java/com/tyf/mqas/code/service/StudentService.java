package com.tyf.mqas.code.service;

import com.alibaba.fastjson.JSONArray;
import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.base.datapage.PageGetter;
import com.tyf.mqas.code.dao.StudentRepository;
import com.tyf.mqas.code.entity.Student;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@Transactional
public class StudentService extends PageGetter<Student>{

    @Autowired
    private StudentRepository studentRepository;

    /**
     * 分页查询
     * @param parameterMap
     * @return
     */
    public DataPage<Student> getDataPage(Map<String,String[]> parameterMap){
        return super.getPages(parameterMap);
    }

    /**
     * 保存
     * @param student
     * @return
     */
    public Student saveEntity(Student student){
        return studentRepository.save(student);
    }

    /**
     * 删除实体
     * @param id
     */
    public void deleteStudent(Integer id){
        studentRepository.deleteById(id);
    }

    /**
     * 根据id获取实体
     * @param id
     * @return
     */
    public Student getStudentById(Integer id){
        return studentRepository.getOne(id);
    }

    /**
     * 判断重复
     * @param authority
     * @param id
     * @return
     */
    public boolean verifyTheRepeat(String authority,String id){
        Integer num = null;
        if(StringUtils.isNotBlank(id)){
            num = studentRepository.getStudentNumByIdAndAuthority(Integer.parseInt(id), authority);
        }else{
            num = studentRepository.getStudentNumByAuthority( authority);
        }
        return num > 0?false:true;
    }

}