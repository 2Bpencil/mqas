package com.tyf.mqas.code.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.code.entity.Student;
import com.tyf.mqas.code.entity.WrongQuestion;
import com.tyf.mqas.code.service.StudentService;
import com.tyf.mqas.code.service.WrongQuestionService;
import com.tyf.mqas.utils.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/**

/**
 * @ClassName WrongQuestionController
 * @Description:
 * @Author tyf
 * @Date 2019年08月23日
 * @Version V1.0
 **/
@Controller
@RequestMapping("/wrongQuestion")
public class WrongQuestionController {

    private final static Logger logger = LoggerFactory.getLogger(WrongQuestionController.class);

    @Autowired
    private WrongQuestionService wrongQuestionService;
    @Autowired
    private StudentService studentService;

    /**
     * 错题管理
     * @param request
     * @return
     */
    @RequestMapping(value = "wrongQuestionManage",method = RequestMethod.GET)
    public ModelAndView wrongQuestionManage(HttpServletRequest request){
        String id = request.getParameter("id");
        Student student = studentService.getStudentById(Integer.parseInt(id));
        ModelAndView modelAndView = new ModelAndView("/service/wrongQuestion");
        modelAndView.addObject("student",student);
        return modelAndView;
    }

    /**
     * 错题分析
     * @param request
     * @return
     */
    @RequestMapping(value = "analysisOfMistakenQuestion",method = RequestMethod.GET)
    public ModelAndView analysisOfMistakenQuestion(HttpServletRequest request){
        String id = request.getParameter("id");
        Student student = studentService.getStudentById(Integer.parseInt(id));
        ModelAndView modelAndView = new ModelAndView("service/analysisOfMistakenQuestion");
        modelAndView.addObject("student",student);
        return modelAndView;
    }

    /**
     * 分页查询
     * @param request
     * @param response
     */
    @RequestMapping(value = "getTableJson",method = RequestMethod.POST)
    public void getTableJson(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("studentId");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        Map<String,String[]> parameterMap = request.getParameterMap();
        DataPage<WrongQuestion> pages = wrongQuestionService.getDataPage(parameterMap,Integer.parseInt(id),startTime,endTime);
        String json = JSONObject.toJSONStringWithDateFormat(pages, "yyyy-MM-dd");
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
    @RequestMapping(value = "deleteWrongQuestion",method = RequestMethod.POST)
    public void deleteRole(HttpServletRequest request, HttpServletResponse response){
        int flag = 1;
        String id = request.getParameter("id");
        try{
            wrongQuestionService.deleteWrongQuestion(Integer.parseInt(id));
            logger.info(SecurityUtil.getCurUserName()+"删除错题成功");
        }catch (Exception e){
            flag = 0;
            logger.error(SecurityUtil.getCurUserName()+"删除错题失败");
        }
        try {
            response.getWriter().print(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 知识点错误次数
     */
    @RequestMapping(value = "knowledgePointErrorNum",method = RequestMethod.POST)
    public void knowledgePointErrorNum(HttpServletRequest request, HttpServletResponse response){
        String studentId = request.getParameter("studentId");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String json = wrongQuestionService.getknowledgePointErrorNumJson(Integer.parseInt(studentId),startDate,endDate);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取该学生所有错误知识点
     * @param request
     * @param response
     */
    @RequestMapping(value = "getAllKnowledge",method = RequestMethod.POST)
    public void getAllKnowledge(HttpServletRequest request, HttpServletResponse response){
        String studentId = request.getParameter("studentId");
        String json = wrongQuestionService.getAllKnowledge(Integer.parseInt(studentId));
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 知识点频次分析
     * @param request
     * @param response
     */
    @RequestMapping(value = "knowledgeFrequency",method = RequestMethod.POST)
    public void knowledgeFrequency(HttpServletRequest request, HttpServletResponse response){
        String studentId = request.getParameter("studentId");
        String code = request.getParameter("code");
        String json =  wrongQuestionService.knowledgeFrequency(Integer.parseInt(studentId),code);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重点知识统计
     * @param request
     * @param response
     */
    @RequestMapping(value = "keyKnowledge",method = RequestMethod.POST)
    public void keyKnowledge(HttpServletRequest request, HttpServletResponse response){
        String studentId = request.getParameter("studentId");
        String json =  wrongQuestionService.keyKnowledge(Integer.parseInt(studentId));
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 }
