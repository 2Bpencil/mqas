package com.tyf.mqas.code.controller;

import com.alibaba.fastjson.JSONObject;
import com.tyf.mqas.base.dataPage.DataPage;
import com.tyf.mqas.code.entity.Role;
import com.tyf.mqas.code.service.RoleService;
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

    private final static Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;
    @RequestMapping(value = "roleManage",method = RequestMethod.GET)
    public String roleManage(){
        return "/system/role";
    }

    /**
     * 分页查询
     * @param request
     * @param response
     */
    @RequestMapping(value = "getTableJson",method = RequestMethod.GET)
    public void getTableJson(HttpServletRequest request, HttpServletResponse response){
        Map<String,String[]> parameterMap = request.getParameterMap();
        DataPage<Role> pages = roleService.getDataPage(parameterMap);
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
    public void saveOrEditEntity(@ModelAttribute("role") Role role, HttpServletRequest request, HttpServletResponse response){
        int flag = 1;
        String oprate = "新增";
        if(role.getId()!=null){
            oprate = "编辑";
        }
        try{
            roleService.saveEntity(role);
            logger.info(oprate+"角色成功");
        }catch (Exception e){
            flag = 0;
            logger.error(oprate+"角色失败");
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
        Role role = roleService.getRoleById(Integer.parseInt(id));
        String json = JSONObject.toJSONString(role);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除角色
     * @param request
     * @param response
     */
    @RequestMapping(value = "deleteRole",method = RequestMethod.GET)
    public void deleteRole(HttpServletRequest request, HttpServletResponse response){
        int flag = 1;
        String id = request.getParameter("id");
        try{
            roleService.deleteRole(Integer.parseInt(id));
            logger.info("删除角色成功");
        }catch (Exception e){
            flag = 0;
            logger.error("删除角色失败");
        }
        try {
            response.getWriter().print(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取角色的菜单
     * @param request
     * @param response
     */
    @RequestMapping(value = "getMenusByRoleId",method = RequestMethod.GET)
    public void getMenusByRoleId(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        String json = roleService.getMenusByRoleId(Integer.parseInt(id));
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "saveRoleAndMenu",method = RequestMethod.GET)
    public void saveRoleAndMenu(HttpServletRequest request, HttpServletResponse response){
        int flag = 1;
        String roleId = request.getParameter("roleId");
        String menuIds = request.getParameter("menuIds");
        try{
            roleService.saveRsRoleMenu(Integer.parseInt(roleId),menuIds );
            logger.info("保存菜单成功");
        }catch (Exception e){
            flag = 0;
            logger.info("保存菜单成功");
            e.printStackTrace();
        }
        try {
            response.getWriter().print(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
    