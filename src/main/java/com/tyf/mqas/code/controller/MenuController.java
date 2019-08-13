package com.tyf.mqas.code.controller;

import com.tyf.mqas.code.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/menu")
public class MenuController {

    private final static Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "menuManage",method = RequestMethod.GET)
    public String menuManage(){
        return "system/menu";
    }

    /**
     * 获取所有菜单
     * @param request
     * @param response
     */
    @RequestMapping(value = "getAllMenus",method = RequestMethod.GET)
    public void getAllMenus(HttpServletRequest request, HttpServletResponse response){
        String json = menuService.getAllMenus();
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
