package com.tyf.mqas.code.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.code.entity.Knowledge;
import com.tyf.mqas.code.service.KnowledgeService;
import com.tyf.mqas.utils.SecurityUtil;
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
import java.util.UUID;


/**

/**
 * @ClassName KnowledgeController
 * @Description: TODO
 * @Author tyf
 * @Date 2019年08月16日
 * @Version V1.0
 **/
@Controller
@RequestMapping("/knowledge")
public class KnowledgeController {

    private final static Logger logger = LoggerFactory.getLogger(KnowledgeController.class);

    @Autowired
    private KnowledgeService knowledgeService;

    @RequestMapping(value = "knowledgeManage",method = RequestMethod.GET)
    public String knowledgeManage(){
        return "service/knowledge";
    }

    /**
     * 获取所有
     * @param request
     * @param response
     */
    @RequestMapping(value = "getAllKnowledges",method = RequestMethod.POST)
    public void getAllKnowledges(HttpServletRequest request, HttpServletResponse response){
        String json = knowledgeService.getAllKnowledges();
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
    public void saveOrEditEntity(@ModelAttribute("knowledge") Knowledge knowledge, HttpServletRequest request, HttpServletResponse response){
        int flag = 1;
        String oprate = "新增";
        if(knowledge.getId()!=null){
            oprate = "编辑";
            Knowledge oldKnowledge = knowledgeService.getKnowledgeById(knowledge.getId());
            oldKnowledge.setName(knowledge.getName());
            oldKnowledge.setSort(knowledge.getSort());
            oldKnowledge.setType(knowledge.getType());
            knowledge = oldKnowledge;
        }else{
           //新增
            if(knowledge.getPid()==null){
                knowledge.setPid(0);
            }
            String uuid = UUID.randomUUID().toString();
            knowledge.setCode(uuid);
            if(knowledge.getPid()!=0){
                Knowledge parentKnowledge = knowledgeService.getKnowledgeById(knowledge.getPid());
                knowledge.setPath(parentKnowledge.getPath()+"#"+uuid);
            }else{
                knowledge.setPath(uuid);
            }
        }
        try{
            knowledgeService.saveEntity(knowledge);
            logger.info(SecurityUtil.getCurUserName()+"---"+oprate+"知识成功");
        }catch (Exception e){
            flag = 0;
            logger.error(SecurityUtil.getCurUserName()+"---"+oprate+"知识失败");
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
        Knowledge knowledge = knowledgeService.getKnowledgeById(Integer.parseInt(id));
        String json = JSONObject.toJSONString(knowledge);
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
        String json = knowledgeService.getEditInfo(Integer.parseInt(id));
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
    @RequestMapping(value = "deleteKnowledge",method = RequestMethod.POST)
    public void deleteKnowledge(HttpServletRequest request, HttpServletResponse response){
        int flag = 1;
        String ids = request.getParameter("ids");
        try{
            knowledgeService.deleteKnowledge(ids);
            logger.info(SecurityUtil.getCurUserName()+"---"+"删除知识成功");
        }catch (Exception e){
            e.printStackTrace();
            flag = 0;
            logger.error(SecurityUtil.getCurUserName()+"---"+"删除知识失败");
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
        boolean isExist = knowledgeService.verifyTheRepeat(name, id);
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
    @RequestMapping(value = "checkKnowledgeUsed",method = RequestMethod.POST)
    public void checkKnowledgeUsed(HttpServletRequest request, HttpServletResponse response){
        String ids = request.getParameter("ids");
        boolean flag = knowledgeService.checkKnowledgeUsed(ids);
        try {
            response.getWriter().print(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ztree
     * @param request
     * @param response
     */
    @RequestMapping(value = "getAllKnowledgeForTree",method = RequestMethod.GET)
    public void getAllKnowledgeForTree(HttpServletRequest request, HttpServletResponse response){
        String json = knowledgeService.getAllKnowledges();
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 }
