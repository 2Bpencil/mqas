package com.tyf.mqas.code.controller;

import com.alibaba.fastjson.JSONObject;
import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.code.entity.Student;
import com.tyf.mqas.code.service.StudentService;
import com.tyf.mqas.utils.SecurityUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/**

/**
 * @ClassName StudentController
 * @Description: 学生
 * @Author tyf
 * @Date 2019年08月18日
 * @Version V1.0
 **/
@Controller
@RequestMapping("/student")
public class StudentController {

    private final static Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "studentManage",method = RequestMethod.GET)
    public String studentManage(){
        return "service/student";
    }

    /**
     * 分页查询
     * @param request
     * @param response
     */
    @RequestMapping(value = "getTableJson",method = RequestMethod.POST)
    public void getTableJson(HttpServletRequest request, HttpServletResponse response){
        String classesId = request.getParameter("classesId");
        //如果id为空，赋值0避免查询报错
        if(StringUtils.isBlank(classesId)){
            classesId = "0";
        }
        Map<String,String[]> parameterMap = request.getParameterMap();
        DataPage<Student> pages = studentService.getDataPage(parameterMap,classesId);
        String json = JSONObject.toJSONString(pages);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * 保存或者编辑实体
    */
    @RequestMapping(value = "saveOrEditEntity",method = RequestMethod.POST)
    public void saveOrEditEntity(@ModelAttribute("student") Student student, HttpServletRequest request, HttpServletResponse response){
        String classesId = request.getParameter("classesId");
        int flag = 1;
        String oprate = "新增";
        if(student.getId()!=null){
            oprate = "编辑";
        }
        try{
            studentService.saveEntity(student,Integer.parseInt(classesId));
            logger.info(SecurityUtil.getCurUserName()+oprate+"学生成功");
        }catch (Exception e){
            flag = 0;
            logger.error(SecurityUtil.getCurUserName()+oprate+"学生失败");
        }
        try {
            response.getWriter().print(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
    * 获取实体信息
    * @param request
    * @param response
    */
    @RequestMapping(value = "getEntityInfo",method = RequestMethod.POST)
    public void getEntityInfo(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        Student student = studentService.getStudentById(Integer.parseInt(id));
        String json = JSONObject.toJSONString(student);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * 删除
    * @param request
    * @param response
    */
    @RequestMapping(value = "deleteStudent",method = RequestMethod.POST)
    public void deleteRole(HttpServletRequest request, HttpServletResponse response){
        int flag = 1;
        String id = request.getParameter("id");
        try{
            studentService.deleteStudent(Integer.parseInt(id));
            logger.info(SecurityUtil.getCurUserName()+"删除学生成功");
        }catch (Exception e){
            flag = 0;
            logger.error(SecurityUtil.getCurUserName()+"删除学生失败");
        }
        try {
            response.getWriter().print(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





    /**
    * 验证重复
    * @param request
    * @param response
    */
    @RequestMapping(value = "verifyTheRepeat",method = RequestMethod.POST)
    public void verifyTheRepeat(HttpServletRequest request, HttpServletResponse response){
        String authority = request.getParameter("authority");
        String id = request.getParameter("id");
        boolean isExist = studentService.verifyTheRepeat(authority, id);
        try {
            response.getWriter().print(isExist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



 }
