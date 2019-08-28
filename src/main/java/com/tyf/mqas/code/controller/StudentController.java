package com.tyf.mqas.code.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.code.entity.Student;
import com.tyf.mqas.code.service.StudentService;
import com.tyf.mqas.utils.PoiUtil;
import com.tyf.mqas.utils.SecurityUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
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
    @RequestMapping(value = "studentWrongQuestions",method = RequestMethod.GET)
    public ModelAndView studentWrongQuestions(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("service/detail");
        return modelAndView;
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
        String json = JSONObject.toJSONStringWithDateFormat(pages, "yyyy-MM-dd HH:mm:ss");
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
        int flag = 1;
        String oprate = "新增";
        if(student.getId()!=null){
            Student old = studentService.getStudentById(student.getId());
            student.setTime(old.getTime());
            oprate = "编辑";
        }else{
            student.setTime(PoiUtil.DATEFORMAT_TIME.format(new Date()));
        }
        try{
            studentService.saveEntity(student);
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

    /**
     * 下载导入模板
     * @param request
     * @param response
     */
    @RequestMapping(value = "downloadTemplates",method = RequestMethod.GET)
    public void downloadTemplates(HttpServletRequest request, HttpServletResponse response){
        Resource resource =  new ClassPathResource("static/download_file/test.xls");
        try {
            InputStream is = resource.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
            // 设置在下载框默认显示的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(("错题模板.xls").getBytes(), "iso-8859-1"));
            // 指明response的返回对象是文件流
            response.setContentType("application/octet-stream");
            // 读出文件到response
            // 这里是先需要把要把文件内容先读到缓冲区
            // 再把缓冲区的内容写到response的输出流供用户下载
            byte[] b = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(b);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(b);
            // 人走带门
            bufferedInputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存错题
     * @param request
     * @param response
     */
    @RequestMapping(value = "saveWrongQuestion",method = RequestMethod.POST)
    public void saveWrongQuestion(HttpServletRequest request, HttpServletResponse response) {
        int flag = 1;
        String id = request.getParameter("studentId");
        try{
            studentService.saveWrongQuestion(request,Integer.parseInt(id));
            logger.info(SecurityUtil.getCurUserName()+"保存错题信息成功");
        }catch (Exception e){
            flag = 0;
            logger.error(SecurityUtil.getCurUserName()+"保存错题信息失败");
        }
        try {
            response.getWriter().print(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



 }
