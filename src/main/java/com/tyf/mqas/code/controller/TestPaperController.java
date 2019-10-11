package com.tyf.mqas.code.controller;

import com.alibaba.fastjson.JSONObject;
import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.code.entity.TestPaper;
import com.tyf.mqas.code.entity.TestPaperType;
import com.tyf.mqas.code.entity.TestQuestion;
import com.tyf.mqas.code.service.TestPaperService;
import com.tyf.mqas.utils.PoiUtil;
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
/**
* @Description: 试卷管理
* @Author: Mr.Tan
* @Date: 2019/10/10 16:03
*/
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
        Map<String,String[]> parameterMap = request.getParameterMap();
        DataPage<TestPaper> pages = testPaperService.getDataPage(parameterMap);
        String json = JSONObject.toJSONString(pages);
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
        testPaper.setTime(PoiUtil.DATEFORMAT_TIME.format(new Date()));

        try{
            testPaperService.saveOrEditTestPaper(testPaper,request);
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

    /**
    * @Description: 下载
    * @Param:
    * @return:
    * @Author: Mr.Tan
    * @Date: 2019/10/10 19:39
    */
    @RequestMapping(value = "downloadTestPaper",method = RequestMethod.GET)
    public void downloadTestPaper(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        testPaperService.downloadTestPaper(response,Integer.parseInt(id));
    }




    /**
     * 验证重复
     * @param request
     * @param response
     */
    @RequestMapping(value = "verifyTheRepeat",method = RequestMethod.POST)
    public void verifyTheRepeat(HttpServletRequest request, HttpServletResponse response){
        String name = request.getParameter("name");
        boolean isExist = testPaperService.verifyTheRepeat(name);
        try {
            response.getWriter().print(isExist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** 
    * @Description: 删除
    * @Param:  
    * @return:  
    * @Author: Mr.Tan 
    * @Date: 2019/10/10 19:04
    */ 
    @RequestMapping(value = "deleteTestPaper",method = RequestMethod.POST)
    public void deleteTestPaper(HttpServletRequest request, HttpServletResponse response){
        int flag = 1;
        String id = request.getParameter("id");
        try {
            testPaperService.deleteTestPaper(Integer.parseInt(id));
        } catch (Exception e) {
            logger.error(SecurityUtil.getCurUserName()+"-删除-试卷失败");
            flag = 0;
        }
        try {
            response.getWriter().print(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
