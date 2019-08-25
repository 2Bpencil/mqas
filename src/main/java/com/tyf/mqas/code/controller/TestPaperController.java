package com.tyf.mqas.code.controller;

import com.alibaba.fastjson.JSONObject;
import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.code.entity.TestQuestion;
import com.tyf.mqas.code.service.TestPaperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/testPaper")
public class TestPaperController {

    private final static Logger logger = LoggerFactory.getLogger(TestPaperController.class);
    @Autowired
    private TestPaperService testPaperService;


    @RequestMapping(value = "testPaperManage",method = RequestMethod.GET)
    public String roleManage(){
        return "service/testPaper";
    }


    /**
     * 分页查询
     * @param request
     * @param response
     */
    @RequestMapping(value = "getTableJson",method = RequestMethod.POST)
    public void getTableJson(HttpServletRequest request, HttpServletResponse response){
        Map<String,String[]> parameterMap = request.getParameterMap();
        DataPage<TestQuestion> pages = testPaperService.getDataPage(parameterMap);
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




    }
    //保存
    @RequestMapping(value = "saveOrEditTestPaper",method = RequestMethod.POST)
    public void saveOrEditTestPaper(HttpServletRequest request, HttpServletResponse response){

    }
    @RequestMapping(value = "saveOrEditTestPaperType",method = RequestMethod.POST)
    public void saveOrEditTestPaperType(HttpServletRequest request, HttpServletResponse response){

    }
    @RequestMapping(value = "saveOrEditTestQuestion",method = RequestMethod.POST)
    public void saveOrEditTestQuestion(HttpServletRequest request, HttpServletResponse response){

    }
    //删除
    @RequestMapping(value = "deletePaper",method = RequestMethod.POST)
    public void deleteTestPaper(HttpServletRequest request, HttpServletResponse response){

    }
    @RequestMapping(value = "deletePaper",method = RequestMethod.POST)
    public void deleteTestPaperType(HttpServletRequest request, HttpServletResponse response){

    }
    @RequestMapping(value = "deletePaper",method = RequestMethod.POST)
    public void deleteTestQuestion(HttpServletRequest request, HttpServletResponse response){

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
