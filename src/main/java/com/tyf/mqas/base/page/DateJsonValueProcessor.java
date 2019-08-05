package com.tyf.mqas.base.page;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: tyf
 * @Date: 2018/11/6 09:30
 * @Description:
 */
public class DateJsonValueProcessor implements JsonValueProcessor {

    private String format = "yyyy-MM-dd HH:mm:ss";

    public DateJsonValueProcessor(){

    }

    public DateJsonValueProcessor(String format){
        this.format = format;
    }

    @Override
    public Object processArrayValue(Object arg0, JsonConfig arg1) {
        String[] obj = {};
        if (arg0 instanceof Date[]) {
            SimpleDateFormat sf = new SimpleDateFormat(format);
            Date[] dates = (Date[]) arg0;
            obj = new String[dates.length];
            for (int i = 0; i < dates.length; i++) {
                obj[i] = sf.format(dates[i]);
            }
        }
        return obj;
    }

    @Override
    public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
        if (arg1 instanceof Date) {
            String str = new SimpleDateFormat(format).format((Date) arg1);
            return str;
        }
        return arg1;
    }
}
