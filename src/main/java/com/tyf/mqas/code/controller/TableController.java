package com.tyf.mqas.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/table")
public class TableController {

    @RequestMapping(value = "toTable",method = RequestMethod.GET)
    public String toTable(){
        return "table";
    }

    @RequestMapping(value = "getTableJson",method = RequestMethod.GET)
    public void getTableJson(HttpServletRequest request, HttpServletResponse response){

        String json = "{\"page\":\"1\",\"total\":2,\t\t\t\"records\":\"13\",\t\t\t\"rows\":\t\t\t\t\t[\t\t\t\t\t\t{\t\t\t\t\t\t\t\"id\":\"13\",\t\t\t\t\t\t\t\"cell\":\t\t\t\t\t\t\t\t\t[\"13\",\"2007-10-06\",\"Client 3\",\"1000.00\",\"0.00\",\"1000.00\",null]\t\t\t\t\t\t},\t\t\t\t\t\t{\t\t\t\t\t\t\t\"id\":\"12\",\t\t\t\t\t\t\t\"cell\":\t\t\t\t\t\t\t\t\t[\"12\",\"2007-10-06\",\"Client 2\",\"700.00\",\"140.00\",\"840.00\",null]\t\t\t\t\t\t},\t\t\t\t\t\t{\t\t\t\t\t\t\t\"id\":\"11\",\t\t\t\t\t\t\t\"cell\":\t\t\t\t\t\t\t\t\t[\"11\",\"2007-10-06\",\"Client 1\",\"600.00\",\"120.00\",\"720.00\",null]\t\t\t\t\t\t},\t\t\t\t\t\t{\t\t\t\t\t\t\t\"id\":\"10\",\t\t\t\t\t\t\t\"cell\":\t\t\t\t\t\t\t\t\t[\"10\",\"2007-10-06\",\"Client 2\",\"100.00\",\"20.00\",\"120.00\",null]\t\t\t\t\t\t},\t\t\t\t\t\t{\t\t\t\t\t\t\t\"id\":\"9\",\t\t\t\t\t\t\t\"cell\":\t\t\t\t\t\t\t\t\t[\"9\",\"2007-10-06\",\"Client 1\",\"200.00\",\"40.00\",\"240.00\",null]\t\t\t\t\t\t},\t\t\t\t\t\t{\t\t\t\t\t\t\t\"id\":\"8\",\t\t\t\t\t\t\t\"cell\":\t\t\t\t\t\t\t\t\t[\"8\",\"2007-10-06\",\"Client 3\",\"200.00\",\"0.00\",\"200.00\",null]\t\t\t\t\t\t},\t\t\t\t\t\t{\t\t\t\t\t\t\t\"id\":\"7\",\t\t\t\t\t\t\t\"cell\":\t\t\t\t\t\t\t\t\t[\"7\",\"2007-10-05\",\"Client 2\",\"120.00\",\"12.00\",\"134.00\",null]\t\t\t\t\t\t},\t\t\t\t\t\t{\t\t\t\t\t\t\t\"id\":\"6\",\t\t\t\t\t\t\t\"cell\":\t\t\t\t\t\t\t\t\t[\"6\",\"2007-10-05\",\"Client 1\",\"50.00\",\"10.00\",\"60.00\",\"\"]\t\t\t\t\t\t},\t\t\t\t\t\t{\t\t\t\t\t\t\t\"id\":\"5\",\t\t\t\t\t\t\t\"cell\":\t\t\t\t\t\t\t\t\t[\"5\",\"2007-10-05\",\"Client 3\",\"100.00\",\"0.00\",\"100.00\",\"no tax at all\"]\t\t\t\t\t\t},\t\t\t\t\t\t{\t\t\t\t\t\t\t\"id\":\"4\",\t\t\t\t\t\t\t\"cell\":\t\t\t\t\t\t\t\t\t[\"4\",\"2007-10-04\",\"Client 3\",\"150.00\",\"0.00\",\"150.00\",\"no tax\"]\t\t\t\t\t\t}\t\t\t\t\t],\t\t\t\"userdata\":{\"amount\":3220,\"tax\":342,\"total\":3564,\"name\":\"Totals:\"}\t\t}";
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
