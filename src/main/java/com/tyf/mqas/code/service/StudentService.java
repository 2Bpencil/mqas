package com.tyf.mqas.code.service;

import com.alibaba.fastjson.JSONArray;
import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.base.datapage.PageGetter;
import com.tyf.mqas.code.dao.KnowledgeRepository;
import com.tyf.mqas.code.dao.StudentRepository;
import com.tyf.mqas.code.dao.WrongQuestionRepository;
import com.tyf.mqas.code.entity.Student;
import com.tyf.mqas.code.entity.WrongQuestion;
import com.tyf.mqas.utils.PoiUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@Service
@Transactional
public class StudentService extends PageGetter<Student>{

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private WrongQuestionRepository wrongQuestionRepository;
    @Autowired
    private KnowledgeRepository knowledgeRepository;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 分页查询
     * @param parameterMap
     * @return
     */
    public DataPage<Student> getDataPage(Map<String,String[]> parameterMap,String calssesId){
        String sql = "SELECT student.* FROM student  LEFT JOIN r_classes_student rck ON student.id = rck.student_id WHERE rck.classes_id = "+calssesId;
        String countSql = "SELECT COUNT(*) FROM student  LEFT JOIN r_classes_student rck ON student.id = rck.student_id WHERE rck.classes_id = "+calssesId;
        return super.getPages(parameterMap,sql,countSql);
    }

    /**
     * 保存
     * @param student
     * @return
     */
    public Student saveEntity(Student student,Integer classesId){
        student = studentRepository.save(student);
        studentRepository.deleteRsClassesAndStudent(student.getId());
        studentRepository.saveRsClassesAndStudent(student.getId(),classesId);
        return student;
    }

    /**
     * 删除实体
     * @param id
     */
    public void deleteStudent(Integer id){
        studentRepository.deleteRsClassesAndStudent(id);
        studentRepository.deleteById(id);
    }

    /**
     * 根据id获取实体
     * @param id
     * @return
     */
    public Student getStudentById(Integer id){
        return studentRepository.getStudentById(id);
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

    /**
     * 保存错题信息
     * @param request
     * @param id
     */
    public void saveWrongQuestion(HttpServletRequest request,Integer id){
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mr = (MultipartHttpServletRequest) request;
            Iterator iter = mr.getFileMap().values().iterator();
            if (iter.hasNext()) {
                MultipartFile file = (MultipartFile) iter.next();
                List<String[]> dataList = PoiUtil.readImportFile(file,2);
                List<WrongQuestion> list = new ArrayList<>();
                Date date = new Date();
                dataList.forEach(data->{
                    WrongQuestion wrongQuestion = new WrongQuestion();
                    wrongQuestion.setName(data[1]);
                    wrongQuestion.setKnowledgeCode(data[2]);
                    wrongQuestion.setTime(sdf.format(date));
                    wrongQuestion.setStudentId(id);
                    wrongQuestion.setKnowledgeName(knowledgeRepository.findByCode(data[2]).getName());
                   list.add(wrongQuestion);
                });
                wrongQuestionRepository.saveAll(list);
            }
        }

    }

}