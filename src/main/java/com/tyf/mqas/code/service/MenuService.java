package com.tyf.mqas.code.service;

import com.alibaba.fastjson.JSONArray;
import com.tyf.mqas.code.dao.MenuRepository;
import com.tyf.mqas.code.entity.Menu;
import com.tyf.mqas.code.entity.TreeTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<Menu> list = menuRepository.findAllBySort();
        List<TreeTable> treeTables = new ArrayList<TreeTable>();
        list.forEach(menu -> {
            TreeTable treeTable = new TreeTable();
            treeTable.setId(menu.getId()+"");
            treeTable.setpId(menu.getPid()==0?"":menu.getPid().toString());
            treeTable.setName(menu.getName());
            String[] td = {menu.getCode(),menu.getUrl(),menu.getType()==0?"菜单标题":"应用菜单",menu.getSort()==null?"":menu.getSort().toString()};
            treeTable.setTd(td);
            treeTables.add(treeTable);
        });
        return JSONArray.toJSONString(treeTables);
    }

}
