package com.tyf.mqas.code.dao;

import com.tyf.mqas.base.repository.ExpandJpaRepository;
import com.tyf.mqas.code.entity.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends ExpandJpaRepository<Menu,Integer> {

    @Query(value = "select m.* from menu m left join r_role_menu rm on m.id = rm.menu_id left join role r on r.id = rm.role_id where r.id = ?1 ", nativeQuery = true)
    List<Menu> findByRoleId(Integer roleId);
    @Query(value = "select m.* from menu m left join r_role_menu rm on m.id = rm.menu_id left join role r on r.id = rm.role_id where r.id in (select r2.id " +
            " from role r2 left join r_user_role ur on ur.role_id = r2.id where ur.user_id = ?1) ", nativeQuery = true)
    List<Menu> findByUserId(Integer userId);



}
