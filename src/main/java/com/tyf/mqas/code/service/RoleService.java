package com.tyf.mqas.code.service;

import com.tyf.mqas.code.entity.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAll();

    List<Role> findByMenuId(Integer id);

}
