package com.tyf.mqas.code.controller;

import com.alibaba.fastjson.JSONObject;
import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.code.entity.Role;
import com.tyf.mqas.code.entity.TestPaper;
import com.tyf.mqas.code.entity.TestPaperType;
import com.tyf.mqas.code.entity.TestQuestion;
import com.tyf.mqas.code.service.TestPaperService;
import com.tyf.mqas.utils.DateUtil;
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
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/testPaper")
public class TestPaperController {

    private final static Logger logger = LoggerFactory.getLogger(TestPaperController.class);
    @Autowired
    private TestPaperService testPaperService;


    @RequestMapping(value = "testPaperManage",method = RequestMethod.GET)
    public String testPaperManage(){
        return "service/testPaper";
    }


    /**
     * 分页查询
     * @param request
     * @param response
     */
    @RequestMapping(value = "getTableJson",method = RequestMethod.POST)
    public void getTableJson(HttpServletRequest request, HttpServletResponse response){
        String testPaperTypeId = request.getParameter("testPaperTypeId");
        if(StringUtils.isBlank(testPaperTypeId)){
            testPaperTypeId = "0";
        }
        Map<String,String[]> parameterMap = request.getParameterMap();
        DataPage<TestQuestion> pages = testPaperService.getDataPage(parameterMap,testPaperTypeId);
        String json = JSONObject.toJSONString(pages);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取考试树
     * @param request
     * @param response
     */
    @RequestMapping(value = "getTestPaperTree",method = RequestMethod.GET)
    public void getTestPaperTree(HttpServletRequest request, HttpServletResponse response){
        String json = testPaperService.getTestPaperTreeJson();
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存
     * @param testPaper
     * @param request
     * @param response
     */
    @RequestMapping(value = "saveOrEditTestPaper",method = RequestMethod.POST)
    public void saveOrEditTestPaper(@ModelAttribute("testPaper") TestPaper testPaper, HttpServletRequest request, HttpServletResponse response){
        int flag = 1;
        String oprate = "新增";
        if(testPaper.getId()!=null){
            oprate = "编辑";
        }else{
            testPaper.setTime(DateUtil.sdf.format(new Date()));
        }
        try{
            testPaperService.saveOrEditTestPaper(testPaper);
            logger.info(SecurityUtil.getCurUserName()+oprate+"试卷成功");
        }catch (Exception e){
            flag = 0;
            logger.error(SecurityUtil.getCurUserName()+oprate+"试卷失败");
            e.printStackTrace();
        }
        try {
            response.getWriter().print(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value = "saveOrEditTestPaperType",method = RequestMethod.POST)
    public void saveOrEditTestPaperType(@ModelAttribute("testPaperType") TestPaperType testPaperType, HttpServletRequest request, HttpServletResponse response){
        int flag = 1;
        String oprate = "新增";
        if(testPaperType.getId()!=null){
            oprate = "编辑";
        }
        try{
            testPaperService.saveOrEditTestPaperType(testPaperType);
            logger.info(SecurityUtil.getCurUserName()+oprate+"试卷题型成功");
        }catch (Exception e){
            flag = 0;
            logger.error(SecurityUtil.getCurUserName()+oprate+"试卷题型失败");
            e.printStackTrace();
        }
        try {
            response.getWriter().print(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value = "saveOrEditTestQuestion",method = RequestMethod.POST)
    public void saveOrEditTestQuestion(@ModelAttribute("testQuestion") TestQuestion testQuestion,HttpServletRequest request, HttpServletResponse response){
        int flag = 1;
        String oprate = "新增";
        if(testQuestion.getId()!=null){
            oprate = "编辑";
        }
        try{
            testPaperService.saveOrEditTestQuestion(testQuestion);
            logger.info(SecurityUtil.getCurUserName()+oprate+"试题成功");
        }catch (Exception e){
            flag = 0;
            logger.error(SecurityUtil.getCurUserName()+oprate+"试题失败");
            e.printStackTrace();
        }
        try {
            response.getWriter().print(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value = "getTestPaperInfo",method = RequestMethod.POST)
    public void getTestPaperInfo(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        String json = JSONObject.toJSONString(testPaperService.getTestPaper(Integer.parseInt(id)));
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value = "getTestPaperTypeInfo",method = RequestMethod.POST)
    public void getTestPaperTypeInfo(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        String json = JSONObject.toJSONString(testPaperService.getTestPaperType(Integer.parseInt(id)));
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value = "getTestQuestionInfo",method = RequestMethod.POST)
    public void getTestQuestionInfo(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        String json = JSONObject.toJSONString(testPaperService.getTestQuestion(Integer.parseInt(id)));
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //删除
    @RequestMapping(value = "deleteTestPaper",method = RequestMethod.POST)
    public void deleteTestPaper(HttpServletRequest request, HttpServletResponse response){
        int flag = 1;
        String id = request.getParameter("id");
        try{
            testPaperService.deleteTestPaper(Integer.parseInt(id));
            logger.info(SecurityUtil.getCurUserName()+"---删除试卷成功");
        }catch (Exception e){
            flag = 0;
            logger.error(SecurityUtil.getCurUserName()+"---删除试卷失败");
        }
        try {
            response.getWriter().print(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value = "deleteTestPaperType",method = RequestMethod.POST)
    public void deleteTestPaperType(HttpServletRequest request, HttpServletResponse response){
        int flag = 1;
        String id = request.getParameter("id");
        try{
            testPaperService.deleteTestPaperType(Integer.parseInt(id));
            logger.info(SecurityUtil.getCurUserName()+"---删除题型成功");
        }catch (Exception e){
            flag = 0;
            logger.error(SecurityUtil.getCurUserName()+"---删除题型失败");
        }
        try {
            response.getWriter().print(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value = "deleteTestQuestion",method = RequestMethod.POST)
    public void deleteTestQuestion(HttpServletRequest request, HttpServletResponse response){
        int flag = 1;
        String id = request.getParameter("id");
        try{
            testPaperService.deleteTestQuestion(Integer.parseInt(id));
            logger.info(SecurityUtil.getCurUserName()+"---删除试题成功");
        }catch (Exception e){
            flag = 0;
            logger.error(SecurityUtil.getCurUserName()+"---删除试题失败");
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
        String name = request.getParameter("name");
        String id = request.getParameter("id");
        boolean isExist = testPaperService.verifyTheRepeat(name, id);
        try {
            response.getWriter().print(isExist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
