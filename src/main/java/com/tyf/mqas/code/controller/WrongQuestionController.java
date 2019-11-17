package com.tyf.mqas.code.controller;

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
        ModelAndView modelAndView = new ModelAndView("service/wrongQuestion");
        modelAndView.addObject("student",student);
        return modelAndView;
    }

    /**
     * 保存错题
     * @param request
     * @param response
     */
    @RequestMapping(value = "saveWrongQuestion",method = RequestMethod.POST)
    public void saveWrongQuestion(@ModelAttribute("wrongQuestion")WrongQuestion wrongQuestion, HttpServletRequest request, HttpServletResponse response) {
        int flag = 1;
        try{
            wrongQuestionService.saveWrongQuestion(request,wrongQuestion);
            logger.info(SecurityUtil.getCurUserName()+"保存错题信息成功");
        }catch (Exception e){
            e.printStackTrace();
            flag = 0;
            logger.error(SecurityUtil.getCurUserName()+"保存错题信息失败");
        }
        try {
            response.getWriter().print(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @Description: 下载
     * @Param:
     * @return:
     * @Author: Mr.Tan
     * @Date: 2019/10/10 19:39
     */
    @RequestMapping(value = "downloadWrongQuestion",method = RequestMethod.GET)
    public void downloadTestPaper(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        wrongQuestionService.downloadWrongQuestion(response,Integer.parseInt(id));
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
    /** 
    * @Description:  导出学生错题
    * @Param:  
    * @return:  
    * @Author: Mr.Tan
    * @Date: 2019/10/12 11:25
    */ 
    @RequestMapping(value = "exportStudentWrongQuestion",method = RequestMethod.GET)
    public void exportStudentWrongQuestion(HttpServletRequest request, HttpServletResponse response){
        String studentId = request.getParameter("studentId");
        String code = request.getParameter("code");
        String level = request.getParameter("level");
        String export_start_time = request.getParameter("export_start_time");
        String export_end_time = request.getParameter("export_end_time");
        wrongQuestionService.exportStudentWrongQuestion(Integer.parseInt(studentId),code,level,export_start_time,export_end_time,response);
    }
    /** 
    * @Description: 判断是否有文件
    * @Param:  
    * @return:  
    * @Author: Mr.Tan 
    * @Date: 2019/10/12 17:16
    */ 
    @RequestMapping(value = "hasStudentWrongQuestion",method = RequestMethod.POST)
    public void hasStudentWrongQuestion(HttpServletRequest request, HttpServletResponse response){
        boolean flag = false;
        String studentId = request.getParameter("studentId");
        String code = request.getParameter("code");
        String level = request.getParameter("level");
        String export_start_time = request.getParameter("export_start_time");
        String export_end_time = request.getParameter("export_end_time");
        flag = wrongQuestionService.hasStudentWrongQuestion(Integer.parseInt(studentId),code,level,export_start_time,export_end_time);
        try {
            response.getWriter().print(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * @Description:  导出年级班级错题
    * @Param:
    * @return:
    * @Author: Mr.Tan
    * @Date: 2019/10/14 9:52
    */
    @RequestMapping(value = "exportClassWrongQuestion",method = RequestMethod.GET)
    public void exportClassWrongQuestion(HttpServletRequest request, HttpServletResponse response){
        String type = request.getParameter("type");
        String classId = request.getParameter("classId");
        String code = request.getParameter("code");
        String level = request.getParameter("level");
        String export_start_time = request.getParameter("export_start_time");
        String export_end_time = request.getParameter("export_end_time");
        wrongQuestionService.exportClassWrongQuestion(Integer.parseInt(classId),Integer.parseInt(type),code,level,export_start_time,export_end_time,response);
    }
    /**
    * @Description: 判断年级或者班级时候有错题可以导出
    * @Param:
    * @return:
    * @Author: Mr.Tan
    * @Date: 2019/10/14 9:52
    */
    @RequestMapping(value = "hasClassWrongQuestion",method = RequestMethod.POST)
    public void hasClassWrongQuestion(HttpServletRequest request, HttpServletResponse response){
        boolean flag = false;
        //0 班级  1年级
        String type = request.getParameter("type");
        String classId = request.getParameter("classId");
        String code = request.getParameter("code");
        String level = request.getParameter("level");
        String export_start_time = request.getParameter("export_start_time");
        String export_end_time = request.getParameter("export_end_time");
        flag = wrongQuestionService.hasClassWrongQuestion(Integer.parseInt(classId),Integer.parseInt(type),code,level,export_start_time,export_end_time);
        try {
            response.getWriter().print(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 上传图片进行识别
     * @Param:
     * @return:
     * @Author: Mr.Tan
     * @Date: 2019/10/14 9:52
     */
    @RequestMapping(value = "uploadOcr",method = RequestMethod.POST)
    public void uploadOcr(HttpServletRequest request, HttpServletResponse response){
        String content = wrongQuestionService.ocr(request);
        try {
            response.getWriter().print(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 }
