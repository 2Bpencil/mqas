package com.tyf.mqas.code.dao;

import com.tyf.mqas.base.repository.ExpandJpaRepository;
import com.tyf.mqas.code.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends ExpandJpaRepository<User,Integer> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO r_classes_user(user_id,classes_id) VALUES(?1,?2)", nativeQuery = true)
    void saveClassSet(Integer userId,Integer classesId);

    @Transactional
    @Modifying
    @Query(value = "delete from r_classes_user where user_id = ?1", nativeQuery = true)
    void deleteClassAndUser(Integer userId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO r_user_role(user_id,role_id) VALUES(?1,?2)", nativeQuery = true)
    void saveRoleAndUser(Integer userId,Integer roleId);


    @Transactional
    @Modifying
    @Query(value = "delete from r_user_role where user_id = ?1", nativeQuery = true)
    void deleteRoleAndUser(Integer userId);



    @Query(value = "select count(*) from user where username = ?2 and id != ?1", nativeQuery = true)
    Integer getUserNumByIdAndUsername(Integer id,String username);

    @Query(value = "select count(*) from user where username = ?1", nativeQuery = true)
    Integer getRoleNumByUsername(String username);

    User findUserByUsername(String username);

}
