package com.tyf.mqas.code.controller;



import com.tyf.mqas.base.datapage.DataPage;
import com.tyf.mqas.code.entity.Role;
import com.tyf.mqas.code.entity.User;
import com.tyf.mqas.code.service.UserService;
import com.alibaba.fastjson.JSONObject;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/user")
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @RequestMapping(value = "userManage",method = RequestMethod.GET)
    public String userManage(){
        return "system/user";
    }

    /**
     * 获取用户列表
     * @param request
     * @param response
     */
    @RequestMapping(value = "getTableJson",method = RequestMethod.GET)
    public void getTableJson(HttpServletRequest request, HttpServletResponse response){

        Map<String,String[]> parameterMap = request.getParameterMap();
        DataPage<User> pages = userService.getDataPage(parameterMap);
        String json = JSONObject.toJSONString(pages);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 保存或者编辑实体
     */
    @RequestMapping(value = "saveOrEditEntity",method = RequestMethod.GET)
    public void saveOrEditEntity(@ModelAttribute("user") User user, HttpServletResponse response){
        int flag = 1;
        String oprate = "新增";
        if(user.getId()!=null){
            User u = userService.getUserById(user.getId());
            u.setUsername(user.getUsername());
            u.setPhone(user.getPhone());
            u.setSex(user.getSex());
            user = u;
            oprate = "编辑";
        }
        try{
            userService.saveEntity(user);
            logger.info(SecurityUtil.getCurUserName()+oprate+"用户成功");
        }catch (Exception e){
            flag = 0;
            logger.error(SecurityUtil.getCurUserName()+oprate+"用户失败");
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
    @RequestMapping(value = "getEntityInfo",method = RequestMethod.GET)
    public void getEntityInfo(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        User user = userService.getUserById(Integer.parseInt(id));
        Map map = new HashMap();
        map.put("id",user.getId());
        map.put("username",user.getUsername());
        map.put("phone",user.getPhone());
        map.put("sex",user.getSex());
        String json = JSONObject.toJSONString(map);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
