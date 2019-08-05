package com.tyf.mqas.base.page;

import net.sf.json.JsonConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: tyf
 * @Date: 2018/11/6 09:15
 * @Description:
 */
public class Page<T> {

    public static String DATA_TYPE_JSON = "JSON";
    public static String DATA_TYPE_ENTITY = "ENTITY";
    // 默认当前页
    private int page = 1;
    // 每页显示数量
    private int rows = 10;

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    // 总记录数
    private int Total;
    // 对象集合
    private List<T> pageList = new ArrayList<T>();

    private String pageJson;

    // 总页数
    private int totalPage;

    private String dataType = DATA_TYPE_JSON;

    // 设置JsonConfig配置
    private JsonConfig jsonConfig = new JsonConfig();

    public Page() {

    }

    public int getPage() {
        return page - 1;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<T> getPageList() {
        return pageList;
    }

    public void setPageList(List<T> pageList) {
        this.pageList = pageList;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getPageJson() {
        return pageJson;
    }

    public void setPageJson(String pageJson) {
        this.pageJson = pageJson;
    }

    public JsonConfig getJsonConfig() {
        return jsonConfig;
    }

    public void setJsonConfig(JsonConfig jsonConfig) {
        this.jsonConfig = jsonConfig;
    }

    public int getTotalPage() {
        totalPage = getTotal() % getRows() == 0 ? getTotal() / getRows() : getTotal() / getRows() + 1;
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

}
