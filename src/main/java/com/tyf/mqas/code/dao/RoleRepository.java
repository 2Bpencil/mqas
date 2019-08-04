package com.tyf.mqas.code.dao;

import com.tyf.mqas.base.repository.ExpandJpaRepository;
import com.tyf.mqas.code.entity.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends ExpandJpaRepository<Role,Integer> {

    @Query(value = "select r.* from role r left join r_user_role ur on r.id = ur.role_id left join user u on u.id = ur.user_id where u.id = ?1 ", nativeQuery = true)
    List<Role> findByUserId(Integer userId);

    @Query(value = "select r.* from role r left join r_role_menu rm on r.id = rm.role_id left join menu m on m.id = rm.menu_id where m.id = ?1 ", nativeQuery = true)
    List<Role> findByMenuId(Integer menuId);

}
