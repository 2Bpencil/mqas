package com.tyf.mqas.code.controller;

import com.tyf.mqas.code.entity.Role;
import com.tyf.mqas.code.service.RoleService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        PageRequest pageRequest = PageRequest.of(1,10);
        Page<Role> roles = roleService.getPages(pageRequest);
        String json = JSONObject.fromObject(roles).toString();
        System.out.println(json);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
    