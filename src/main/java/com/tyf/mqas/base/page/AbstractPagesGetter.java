package com.tyf.mqas.base.page;

import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: tyf
 * @Date: 2018/11/7 15:46
 * @Description:
 */
public abstract class AbstractPagesGetter<T> {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    private Class<T> entityClass = ReflectionUtils.getClassGenricType(getClass());

    private String tableName;

    public Page<T> getPage(Page<T> page, List<SearchFilter> filterList) {
        int curpage = page.getPage();
        int size = page.getRows();
        String countSql = "select count(*) from " + tableName;
        String querySql = "select * from " + tableName;
        if (filterList != null) {
            String whereSql = SearchUtils.filterWhereForPage(filterList);
            String orderSql = SearchUtils.filterOrderForPage(filterList);
            if (StringUtils.isNotBlank(whereSql)) {
                countSql += " where " + whereSql;
                querySql += " where " + whereSql;
            }
            if (orderSql != null)
                querySql += orderSql;
        }
        querySql += " LIMIT ?,? ";
        int total = jdbcTemplate.queryForObject(countSql, Integer.class);
        List<T> pageList = jdbcTemplate.query(querySql, new BeanPropertyRowMapper(entityClass), new Object[] { curpage * size, size });
        page.setTotal(total);
        if (page.getDataType().equals(Page.DATA_TYPE_JSON)) {
            StringBuilder sb = new StringBuilder(
                    "{" + "\"total\":" + page.getTotalPage() + "," + "\"page\":" + (curpage + 1) + "," + "\"records\":" + total + "," + "\"rows\":");
            sb.append(JSONArray.fromObject(pageList, page.getJsonConfig()).toString()).append("}");
            page.setPageJson(sb.toString());
        } else {
            page.setPageList(pageList);
        }
        return page;
    }

    public Page getPage(Page page, String sql, List<SearchFilter> filterList, Object... cs) {
        int curpage = page.getPage();
        int size = page.getRows();
        String whereSql = null;
        String orderSql = null;
        String orderStr = null;// 用于接收截取后的排序语句
        if (filterList != null) {
            whereSql = SearchUtils.filterWhereForPage(filterList);
            orderSql = SearchUtils.filterOrderForPageSql(filterList);
        }

        // 如果自定义的SQL语句中包含order by 字段，包含自己本身之后的截取，赋值全局变量
        if (sql.toLowerCase().indexOf("order by") > 0) {
            int indexInt = sql.toLowerCase().indexOf(" order by");
            String whereStr = sql.substring(0, indexInt);
            orderStr = sql.substring(indexInt);
            sql = whereStr;
        }

        if (StringUtils.isNotBlank(whereSql)) {
            String groupby = "";
            if (sql.toLowerCase().indexOf("group") > 0) {
                groupby = sql.replaceFirst("^.*[Gg][Rr][Oo][Uu][Pp]\\s+[Bb][Yy]", "");
                sql = sql.replaceFirst("[Gg][Rr][Oo][Uu][Pp]\\s+[Bb][Yy].*$", "");
            }
            if (sql.toLowerCase().indexOf("where") > 0) {
                sql += sql.matches("^.*where\\s*") ? " " : " and ";
            } else {
                sql += " where ";
            }
            sql += whereSql;
            if (StringUtils.isNotEmpty(groupby))
                sql += " group by " + groupby;
        }
        int total = 0;
        if (sql.toLowerCase().contains("distinct")) {
            total = jdbcTemplate.queryForObject("select count(*) from (" + sql + ") as a", cs, Integer.class);
        } else {
            total = jdbcTemplate.queryForObject(
                    "select count(*) "
                            + sql.replaceFirst("^((?![Ff][Rr][Oo][Mm]).)*([Ff][Rr][Oo][Mm])", " FROM ").replaceFirst("\\s+[Gg][Rr][Oo][Uu][Pp]\\s+.*", ""),
                    cs, Integer.class);
        }
        page.setTotal(total);
        List csList = new ArrayList<Object>();
        for (Object o : cs) {
            csList.add(o);
        }
        csList.add(curpage * size);
        csList.add(size);
        // String countSql = "select count(*) from " + tableName;
        // 如果传入的SQL语句中包含order by 字段，先向SQL语句中添加，然后判断前台是否包含排序设置。
        if (StringUtils.isNotBlank(orderSql)) {
            sql += orderSql;
            if (StringUtils.isNotBlank(orderStr)) {
                sql += orderStr.replaceAll(" order by ", ",");
            }
        } else {
            if (StringUtils.isNotBlank(orderStr)) {
                sql += orderStr;
            }
        }
        // System.out.println(sql);
        sql += " LIMIT ?,? ";
        List<Map<String, Object>> pageList = jdbcTemplate.queryForList(sql, csList.toArray());
//		System.out.println(sql);
        if (page.getDataType().equals(Page.DATA_TYPE_JSON)) {
            StringBuilder sb = new StringBuilder(
                    "{" + "\"total\":" + page.getTotalPage() + "," + "\"page\":" + (curpage + 1) + "," + "\"records\":" + total + "," + "\"rows\":");
            JSONArray array = JSONArray.fromObject(pageList, page.getJsonConfig());
            String stringarray = array.toString();
            sb.append(stringarray).append("}");
            page.setPageJson(sb.toString());
        } else {
            page.setPageList(pageList);
            page.setTotal(total);
        }
        return page;
    }


}
