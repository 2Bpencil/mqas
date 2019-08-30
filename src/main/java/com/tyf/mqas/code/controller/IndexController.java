package com.tyf.mqas.code.controller;

import com.tyf.mqas.code.entity.StudentRecords;
import com.tyf.mqas.code.service.ClassesService;
import com.tyf.mqas.code.service.StudentService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName IndexController
 * @Description:
 * @Author tyf
 * @Date 2019/8/27 
 * @Version V1.0
 **/
@Controller
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private ClassesService classesService;
    @Autowired
    private StudentService studentService;



    /**
     * 获取首页班级信息
     */
    @RequestMapping(value = "getClassesInfo",method = RequestMethod.POST)
    public void getClassesInfo(HttpServletRequest request, HttpServletResponse response){
        String json = classesService.getAllClassesInfo();
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取各年级老师信息
     * @param request
     * @param response
     */
    @RequestMapping(value = "getTeacherInfo",method = RequestMethod.POST)
    public void getTeacherInfo(HttpServletRequest request, HttpServletResponse response){
        String json = classesService.getTeacherInfo();
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 学生走势图
     * @param request
     * @param response
     */
    @RequestMapping(value = "studentNumTrend",method = RequestMethod.POST)
    public void studentNumTrend(HttpServletRequest request, HttpServletResponse response){
        String year = request.getParameter("year");
        if(StringUtils.isBlank(year)){
            year = "0";
        }
        String json = studentService.getStudentTotalNumInfo(Integer.parseInt(year));
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 各年级个科目学生数量情况
     * @param request
     * @param response
     */
    @RequestMapping(value = "learningSituation",method = RequestMethod.POST)
    public void learningSituation(HttpServletRequest request, HttpServletResponse response){
        String json = classesService.learningSituation();
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value = "initYearSelect",method = RequestMethod.POST)
    public void initYearSelect(HttpServletRequest request, HttpServletResponse response){
        String json = studentService.initYearSelect();
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
    