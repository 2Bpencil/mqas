package com.tyf.mqas.base.dataPage;/**
 * @Auther: tyf
 * @Date: 2019/8/8 19:28
 * @Description:
 */

import com.tyf.mqas.base.page.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

/**
 * @ClassName PageGetter
 * @Description: TODO
 * @Author tyf
 * @Date 2019/8/8 
 * @Version V1.0
 **/
public class PageGetter<T> {

    @Autowired
    protected JdbcTemplate jdbcTemplate;
    private String tableName;
    private Class<T> entityClass = ReflectionUtils.getClassGenricType(getClass());

    /**
     *
     * @param parameterMap  参数
     * @return
     */
    public DataPage<T> getPages(Map<String,String[]> parameterMap){
        DataPage<T> page = new DataPage<>();
        page.setDraw(Integer.parseInt(parameterMap.get("draw").toString()));
        int start = Integer.parseInt(parameterMap.get("start").toString());
        parameterMap.remove("draw");








        return null;
    }


}
    