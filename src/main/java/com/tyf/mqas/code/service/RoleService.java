package com.tyf.mqas.code.service;

import com.tyf.mqas.base.dataPage.DataPage;
import com.tyf.mqas.base.dataPage.PageGetter;
import com.tyf.mqas.code.dao.MenuRepository;
import com.tyf.mqas.code.dao.RoleRepository;
import com.tyf.mqas.code.entity.Menu;
import com.tyf.mqas.code.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends PageGetter<Role> {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MenuRepository menuRepository;

    /**
     * 获取所有角色并且获取其所有的菜单
     * @return
     */
    public List<Role> findAll() {
        List<Role> allRoles = new ArrayList<>();
        List<Role> roles = roleRepository.findAll();
        roles.forEach(role -> {
            List<Menu> menus = menuRepository.findByRoleId(role.getId());
            role.setMenus(menus);
            allRoles.add(role);
        });
        return allRoles;
    }

    public List<Role> findAllTableData(){
        return roleRepository.findAll();
    }

    public List<Role> findByMenuId(Integer id) {
        return roleRepository.findByMenuId(id);
    }


    /**
     * 分页查询
     * @param parameterMap
     * @return
     */
    public DataPage<Role> getDataPage(Map<String,String[]> parameterMap){
        return super.getPages(parameterMap);
    }

    /**
     * 保存角色
     * @param role
     * @return
     */
    public Role saveEntity(Role role){
        return roleRepository.save(role);
    }

    /**
     * 删除实体
     * @param id
     */
    public void deleteRole(Integer id){
        roleRepository.deleteById(id);
    }

    /**
     * 根据id获取实体
     * @param id
     * @return
     */
    public Role getRoleById(Integer id){
        return roleRepository.getOne(id);
    }


    /**
     * 保存角色菜单的关系
     * @param roleId
     * @param menuIds
     */
    public void saveRsRoleMenu(Integer roleId,String menuIds){

    }

}
