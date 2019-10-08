package com.tyf.mqas.code.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.code.entity.Classes;
import com.tyf.mqas.code.entity.Knowledge;
import com.tyf.mqas.code.entity.Student;
import com.tyf.mqas.code.entity.User;
import com.tyf.mqas.code.service.ClassesService;
import com.tyf.mqas.code.service.UserService;
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
import java.util.UUID;


/**

/**
 * @ClassName ClassesController
 * @Description:  班级管理
 * @Author tyf
 * @Date 2019年08月17日
 * @Version V1.0
 **/
@Controller
@RequestMapping("/classes")
public class ClassesController {

    private final static Logger logger = LoggerFactory.getLogger(ClassesController.class);

    @Autowired
    private ClassesService classesService;

    @RequestMapping(value = "classesManage",method = RequestMethod.GET)
    public String classesManage(){
        return "service/classes";
    }

    @RequestMapping(value = "classAnalysis",method = RequestMethod.GET)
    public ModelAndView classAnalysis(HttpServletRequest request){
        String id = request.getParameter("id");
        Classes classes = classesService.getClassesById(Integer.parseInt(id));
        Classes parent = classesService.getClassesById(classes.getPid());
        ModelAndView modelAndView = new ModelAndView("service/analysisOfClass");
        modelAndView.addObject("classId",id);
        modelAndView.addObject("className",parent.getName()+"-"+classes.getName());
        return modelAndView;
    }

    /**
     * 获取所有
     * @param request
     * @param response
     */
    @RequestMapping(value = "getAllClassess",method = RequestMethod.POST)
    public void getAllClassess(HttpServletRequest request, HttpServletResponse response){
        String json = classesService.getAllClassess();
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ztree  分配给用户
     * @param request
     * @param response
     */
    @RequestMapping(value = "getAllClassesTree",method = RequestMethod.GET)
    public void getAllClassesTree(HttpServletRequest request, HttpServletResponse response){
        String json = classesService.getAllClassesTree();
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
    public void saveOrEditEntity(@ModelAttribute("classes") Classes classes, HttpServletRequest request, HttpServletResponse response){
        int flag = 1;
        String oprate = "新增";
        if(classes.getId()!=null){
            oprate = "编辑";
            Classes oldclasses = classesService.getClassesById(classes.getId());
            oldclasses.setName(classes.getName());
            oldclasses.setSort(classes.getSort());
            classes = oldclasses;
        }else{
            //新增
            if(classes.getPid()==null){
                classes.setPid(0);
            }
            String uuid = UUID.randomUUID().toString();
            classes.setCode(uuid);
            if(classes.getPid()!=0){
                Classes parentClasses = classesService.getClassesById(classes.getPid());
                classes.setPath(parentClasses.getPath()+"#"+uuid);
            }else{
                classes.setPath(uuid);
            }
        }
        try{
            classesService.saveEntity(classes);
            logger.info(SecurityUtil.getCurUserName()+"---"+oprate+"班级信息成功");
        }catch (Exception e){
            flag = 0;
            logger.error(SecurityUtil.getCurUserName()+"---"+oprate+"班级信息失败");
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
        Classes classes = classesService.getClassesById(Integer.parseInt(id));
        String json = JSONObject.toJSONString(classes);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取编辑信息
     * @param request
     * @param response
     */
    @RequestMapping(value = "getEditEntityInfo",method = RequestMethod.POST)
    public void getEditEntityInfo(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        String json = classesService.getEditInfo(Integer.parseInt(id));
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
    @RequestMapping(value = "deleteClasses",method = RequestMethod.POST)
    public void deleteClasses(HttpServletRequest request, HttpServletResponse response){
        int flag = 1;
        String ids = request.getParameter("ids");
        try{
            classesService.deleteClasses(ids);
            logger.info(SecurityUtil.getCurUserName()+"---"+"删除班级信息成功");
        }catch (Exception e){
            e.printStackTrace();
            flag = 0;
            logger.error(SecurityUtil.getCurUserName()+"---"+"删除班级信息失败");
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
        String pid = request.getParameter("pid");
        boolean isExist = classesService.verifyTheRepeat(name, id , pid);
        try {
            response.getWriter().print(isExist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     /**
     * 判断是否被使用
     * @param request
     * @param response
     */
    @RequestMapping(value = "checkClassesUsed",method = RequestMethod.POST)
    public void checkClassesUsed(HttpServletRequest request, HttpServletResponse response){
        String ids = request.getParameter("ids");
        boolean flag = classesService.checkClassesUsed(ids);
        try {
            response.getWriter().print(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取已分配的知识
     * @param request
     * @param response
     */
    @RequestMapping(value = "getKnowledgeByClassId",method = RequestMethod.POST)
    public void getKnowledgeByClassId(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        String json = classesService.getKnowledgeByClassId(Integer.parseInt(id));
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存知识配置
     */
    @RequestMapping(value = "saveKnowledgeSet",method = RequestMethod.POST)
    public void saveKnowledgeSet(HttpServletRequest request, HttpServletResponse response){
        int flag = 1;
        String id = request.getParameter("id");
        String knowledgeIds = request.getParameter("knowledgeIds");
        try{
            classesService.saveKnowledgeSet(Integer.parseInt(id),knowledgeIds);
            logger.info(SecurityUtil.getCurUserName()+"保存知识配置成功");
        }catch (Exception e){
            flag = 0;
            logger.info(SecurityUtil.getCurUserName()+"保存知识配置失败");
            e.printStackTrace();
        }
        try {
            response.getWriter().print(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 学生管理班级树
     * @param request
     * @param response
     */
    @RequestMapping(value = "getClassTreeForStudent",method = RequestMethod.GET)
    public void getClassTreeForStudent(HttpServletRequest request, HttpServletResponse response){
        String json = classesService.getClassTreeJsonForStudent();
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 班级知识点错误分布统计图
     * @param request
     * @param response
     */
    @RequestMapping(value = "wrongQuestionDistribution",method = RequestMethod.POST)
    public void wrongQuestionDistribution(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("classId");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String json = classesService.wrongQuestionDistribution(Integer.parseInt(id),startDate,endDate);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 知识点错误TOP5
     * @param request
     * @param response
     */
    @RequestMapping(value = "wrongQuestionTop5",method = RequestMethod.POST)
    public void wrongQuestionTop5(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("classId");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String json = classesService.wrongQuestionTop5Json(Integer.parseInt(id),startDate,endDate);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *知识点错题人数占比（下拉选初始化）
     * @param request
     * @param response
     */
    @RequestMapping(value = "getAllWrongKnowledge",method = RequestMethod.POST)
    public void getAllWrongKnowledge(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("classId");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String json = classesService.getAllWrongKnowledgeJson(Integer.parseInt(id),startDate,endDate);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 知识点错题人数占比
     * @param request
     * @param response
     */
    @RequestMapping(value = "wrongProportion",method = RequestMethod.POST)
    public void wrongProportion(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("classId");
        String code = request.getParameter("code");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String json = classesService.wrongProportionJson(Integer.parseInt(id),code,startDate,endDate);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 }
