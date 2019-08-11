package com.tyf.mqas.code.service;

import com.alibaba.fastjson.JSONArray;
import com.tyf.mqas.code.dao.MenuRepository;
import com.tyf.mqas.code.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: tyf
 * @Date: 2019/7/9 19:55
 * @Description:
 */
@Service
public class MenuService  {

    @Autowired
    private MenuRepository menuRepository;

    /**
     * 根据用户id获取该用户所有的菜单权限json数组
     * @param userId
     * @return
     */
    public String getMenusByUserId(Integer userId){
        List<Menu> list = menuRepository.findByUserId(userId);
        return JSONArray.toJSONString(list);
    }

    /**
     * 获取所有菜单
     * @return
     */
    public String getAllMenus(){
        List<Menu> list = menuRepository.findAll();
        return JSONArray.toJSONString(list);
    }

}
