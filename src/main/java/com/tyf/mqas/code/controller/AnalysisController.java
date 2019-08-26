package com.tyf.mqas.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName AnalysisController
 * @Description: 统计分析接口
 * @Author tyf
 * @Date 2019/8/26 
 * @Version V1.0
 **/
@Controller
@RequestMapping("/analysis")
public class AnalysisController {

    /**
     * 获取首页班级信息
     */
    @RequestMapping(value = "getClassesInfo",method = RequestMethod.POST)
    public void getClassesInfo(HttpServletRequest request, HttpServletResponse response){

    }




}
    