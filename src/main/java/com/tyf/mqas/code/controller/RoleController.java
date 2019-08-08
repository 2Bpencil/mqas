package com.tyf.mqas.code.controller;

import com.tyf.mqas.code.service.RoleService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**

/**
 * @ClassName RoleController
 * @Description: TODO
 * @Author tyf
 * @Date 2019/8/7 
 * @Version V1.0
 **/
@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @RequestMapping(value = "roleManage",method = RequestMethod.GET)
    public String roleManage(){
        return "/system/role";
    }

    @RequestMapping(value = "getTableJson",method = RequestMethod.GET)
    public void getTableJson(HttpServletRequest request, HttpServletResponse response){
        String draw = request.getParameter("draw");
        //数据起始位置
        String start = request.getParameter("start");
        //每页显示的条数
        String length = request.getParameter("length");

        Map<String,String[]> parameterMap = request.getParameterMap();




        Map<String,Object> dataMap = new HashMap<>(4);
        dataMap.put("draw",1);
        dataMap.put("recordsTotal",25);
        dataMap.put("recordsFiltered",25);
        dataMap.put("data",roleService.findAllTableData());

        String json = JSONObject.fromObject(dataMap).toString();
        System.out.println(json);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
    