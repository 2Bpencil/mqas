package com.tyf.mqas.code.service;

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

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MenuRepository menuRepository;

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
     * @param pageable
     * @return
     */
    public Page<Role> getPages(Pageable pageable){
        return roleRepository.findAll(pageable);
    }

}
