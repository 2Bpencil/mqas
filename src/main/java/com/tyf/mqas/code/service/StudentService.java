package com.tyf.mqas.code.service;

import com.alibaba.fastjson.JSONArray;
import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.base.datapage.PageGetter;
import com.tyf.mqas.code.dao.KnowledgeRepository;
import com.tyf.mqas.code.dao.StudentRecordsRepository;
import com.tyf.mqas.code.dao.StudentRepository;
import com.tyf.mqas.code.dao.WrongQuestionRepository;
import com.tyf.mqas.code.entity.Student;
import com.tyf.mqas.code.entity.StudentRecords;
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
import java.time.LocalDate;
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
    @Autowired
    private StudentRecordsRepository studentRecordsRepository;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 分页查询
     * @param parameterMap
     * @return
     */
    public DataPage<Student> getDataPage(Map<String,String[]> parameterMap,String calssesId){
        String sql = "SELECT student.* FROM student WHERE student.classes_id = "+calssesId;
        String countSql = "SELECT COUNT(*) FROM student  WHERE student.classes_id = "+calssesId;
        return super.getPages(parameterMap,sql,countSql);
    }

    /**
     * 保存
     * @param student
     * @return
     */
    public Student saveEntity(Student student){
        student = studentRepository.save(student);
        return student;
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

    /**
     * 获取学生数量走势信息
     * @return
     */
    public String getStudentTotalNumInfo(Integer year){
        LocalDate localDate = LocalDate.now();
        if(year == 0){
            year = localDate.getYear();
        }
        Map<String,Object> dataMap = new HashMap<>();
        List<String> xAxisList = new ArrayList<>();
        List<Integer> dataList = new ArrayList<>();
        List<StudentRecords> list = studentRecordsRepository.findAllByYear(year);
        for (StudentRecords record : list) {
            xAxisList.add(record.getYear()+"-"+record.getMonth());
            dataList.add(record.getTotal());
        }
        dataMap.put("xAxis",xAxisList);
        dataMap.put("data",dataList);
        return JSONArray.toJSONString(dataMap);
    }

    /**
     * 初始化年份下拉选
     * @return
     */
    public String initYearSelect(){
        return JSONArray.toJSONString(studentRecordsRepository.getAllYear());
    }

}