package com.tyf.mqas.code.controller;



import com.tyf.mqas.code.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @RequestMapping(value = "userManage",method = RequestMethod.GET)
    public String userManage(){
        return "user";
    }

    @RequestMapping(value = "getTableJson",method = RequestMethod.GET)
    public void getTableJson(HttpServletRequest request, HttpServletResponse response){
        List<Map<String,Object>> list = new ArrayList<>();
        for (int i = 0; i < 10 ; i++) {
            Map<String,Object> map = new HashMap<>(5);
            map.put("first_name","aaa"+i);
            map.put("last_name","bbb"+i);
            map.put("position","ccc"+i);
            map.put("office","ddd"+i);
            map.put("start_date","eee"+i);
            list.add(map);
        }
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("draw",1);
        dataMap.put("recordsTotal",10);
        dataMap.put("recordsFiltered",10);
        dataMap.put("data",list);



        String json = JSONObject.toJSONString(dataMap);
        System.out.println(json);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
