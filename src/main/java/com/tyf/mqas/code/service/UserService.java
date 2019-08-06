package com.tyf.mqas.code.service;

import com.tyf.mqas.base.page.AbstractPagesGetter;
import com.tyf.mqas.base.page.Page;
import com.tyf.mqas.base.page.SearchFilter;
import com.tyf.mqas.code.dao.RoleRepository;
import com.tyf.mqas.code.dao.UserRepository;
import com.tyf.mqas.code.entity.Role;
import com.tyf.mqas.code.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService extends AbstractPagesGetter<User> implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(name);
        if (user != null) {
            List<Role> roles = roleRepository.findByUserId(user.getId());
            System.out.println(roles.size());
            user.setRoles(roles);
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (Role role : roles) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getAuthority());
                //1：此处将权限信息添加到 GrantedAuthority 对象中，在后面进行全权限验证时会使用GrantedAuthority 对象。
                grantedAuthorities.add(grantedAuthority);
            }
            return user;
        } else {throw new UsernameNotFoundException("admin: " + name + " do not exist!");

        }
    }

    public User saveEntity(User user) {
        return userRepository.save(user);
    }

    /**
     *
     * @param pages
     * @param filterList
     * @return
     */
    public String getPageJson(Page<User> pages, List<SearchFilter> filterList){
        String sql = "SELECT * FROM user";
        return super.getPage(pages, sql, filterList).getPageJson();
    }


}
