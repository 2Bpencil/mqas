package com.tyf.mqas.config.security;

import com.tyf.mqas.code.dao.MenuRepository;
import com.tyf.mqas.code.entity.Menu;
import com.tyf.mqas.code.entity.Role;
import com.tyf.mqas.code.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class SysInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuRepository menuRepository;

    private HashMap<String,Collection<ConfigAttribute>> map = null;

    /**
     * 加载所有的权限
     */
    public void loadResourceDefine(){
        map = new HashMap<>();
        Collection<ConfigAttribute> array;
        ConfigAttribute cfg;
        List<Menu> menus = menuRepository.findAll();
        for (Menu menu: menus ) {
            array = new ArrayList<>();
            List<Role> roles = roleService.findByMenuId(menu.getId());
            for (Role role:roles ) {
                cfg = new SecurityConfig(role.getAuthority());
                //此处只添加了用户的名字，其实还可以添加更多权限的信息，例如请求方法到ConfigAttribute的集合中去。此处添加的信息将会作为MyAccessDecisionManager类的decide的第三个参数。
                array.add(cfg);
            }
            //用权限的getUrl() 作为map的key，用ConfigAttribute的集合作为 value，
            map.put(menu.getUrl(), array);
        }


    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if(map == null){
            loadResourceDefine();
        }
        //object 中包含用户请求的request 信息
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        AntPathRequestMatcher matcher;
        String resUrl;

        for(Iterator<String> iter = map.keySet().iterator(); iter.hasNext(); ) {
            resUrl = iter.next();
            matcher = new AntPathRequestMatcher(resUrl);
            if(matcher.matches(request)) {
                return map.get(resUrl);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
