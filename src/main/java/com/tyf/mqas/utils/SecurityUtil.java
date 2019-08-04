package com.tyf.mqas.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Auther: tyf
 * @Date: 2019/7/8 17:25
 * @Description:
 */
public class SecurityUtil {

    final public static String CURRENT_USER_SESSION = "CurUser";
    final public static String CURRENT_USER_MENU = "curMenu";


    /**
     * 获取当前用户
     * @return
     */
    public static String getCurUserName(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }



}
